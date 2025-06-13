package com.svf.mecatool.business.services.implementations;

import com.svf.mecatool.business.services.InvoiceService;
import com.svf.mecatool.integration.model.*;
import com.svf.mecatool.integration.repositories.InvoiceRepository;
import com.svf.mecatool.integration.repositories.PaymentRepository;
import com.svf.mecatool.integration.repositories.WorkOrderRepository;
import com.svf.mecatool.presentation.dto.InvoiceDTO;
import com.svf.mecatool.presentation.dto.PaymentDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.svf.mecatool.integration.repositories.ClientRepository;
import com.svf.mecatool.integration.repositories.VehicleRepository;
import com.svf.mecatool.integration.repositories.InventoryItemRepository;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.util.Locale;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;
    private final WorkOrderRepository workOrderRepository;
    private final ClientRepository clientRepository;
    private final VehicleRepository vehicleRepository;
    private final InventoryItemRepository inventoryItemRepository;

    public InvoiceServiceImpl(
            InvoiceRepository invoiceRepository,
            PaymentRepository paymentRepository,
            WorkOrderRepository workOrderRepository,
            ClientRepository clientRepository,
            VehicleRepository vehicleRepository,
            InventoryItemRepository inventoryItemRepository
    ) {
        this.invoiceRepository = invoiceRepository;
        this.paymentRepository = paymentRepository;
        this.workOrderRepository = workOrderRepository;
        this.clientRepository = clientRepository;
        this.vehicleRepository = vehicleRepository;
        this.inventoryItemRepository = inventoryItemRepository;
    }

    @Override
    public InvoiceDTO createFromWorkOrder(Long workOrderId) {
        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new EntityNotFoundException("Work order not found"));

        if (workOrder.getTotal() == null || workOrder.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Work order must have a positive total amount");
        }

        Invoice invoice = new Invoice();
        invoice.setWorkOrder(workOrder);
        invoice.setIssueDate(LocalDate.now());
        invoice.setTotal(workOrder.getTotal());
        invoice.setPaidAmount(BigDecimal.ZERO);
        invoice.setStatus(InvoiceStatus.PENDING);

        Invoice saved = invoiceRepository.save(invoice);
        return mapToDTO(saved);
    }

    @Override
    public InvoiceDTO getById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));
        return mapToDTO(invoice);
    }

    @Override
    public List<InvoiceDTO> getAll() {
        return invoiceRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceDTO> getByStatus(InvoiceStatus status) {
        return invoiceRepository.findByStatus(status).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceDTO> getByWorkOrder(Long workOrderId) {
        return invoiceRepository.findByWorkOrderId(workOrderId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentDTO addPayment(Long invoiceId, PaymentDTO paymentDTO) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));

        if (paymentDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }

        BigDecimal remainingAmount = invoice.getTotal().subtract(invoice.getPaidAmount());
        if (paymentDTO.getAmount().compareTo(remainingAmount) > 0) {
            throw new IllegalArgumentException("Payment amount exceeds remaining balance");
        }

        Payment payment = new Payment();
        payment.setInvoice(invoice);
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentDate(paymentDTO.getPaymentDate() != null ? paymentDTO.getPaymentDate() : LocalDate.now());
        payment.setMethod(paymentDTO.getMethod());
        payment.setNote(paymentDTO.getNote());

        Payment savedPayment = paymentRepository.save(payment);

        invoice.setPaidAmount(invoice.getPaidAmount().add(paymentDTO.getAmount()));
        
        if (invoice.getPaidAmount().compareTo(invoice.getTotal()) >= 0) {
            invoice.setStatus(InvoiceStatus.PAID);
        } else {
            invoice.setStatus(InvoiceStatus.PARTIALLY_PAID);
        }

        invoiceRepository.save(invoice);
        return mapToPaymentDTO(savedPayment);
    }

    @Override
    public byte[] generatePdf(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));

        WorkOrder workOrder = invoice.getWorkOrder();
        if (workOrder == null) {
            throw new EntityNotFoundException("Work order not found for invoice");
        }

        Vehicle vehicle = workOrder.getVehicle();
        if (vehicle == null) {
            throw new EntityNotFoundException("Vehicle not found for work order");
        }

        Client client = vehicle.getClient();
        if (client == null) {
            throw new EntityNotFoundException("Client not found for vehicle");
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            document.add(new Paragraph("Invoice Details"));
            document.add(new Paragraph("--------------------------------"));
            document.add(new Paragraph("Invoice ID: " + invoice.getId()));
            document.add(new Paragraph("Issue Date: " + invoice.getIssueDate()));
            document.add(new Paragraph("Total Amount: " + NumberFormat.getCurrencyInstance(new Locale("es", "ES")).format(invoice.getTotal())));
            document.add(new Paragraph("Status: " + invoice.getStatus()));

            document.add(new Paragraph("\nClient Information"));
            document.add(new Paragraph("--------------------------------"));
            document.add(new Paragraph("Name: " + client.getFirstName() + " " + client.getLastName()));
            document.add(new Paragraph("Email: " + client.getEmail()));
            document.add(new Paragraph("Phone: " + client.getPhone()));
            document.add(new Paragraph("Address: " + client.getAddress()));

            document.add(new Paragraph("\nVehicle Information"));
            document.add(new Paragraph("--------------------------------"));
            document.add(new Paragraph("License Plate: " + vehicle.getLicensePlate()));
            document.add(new Paragraph("Brand: " + vehicle.getBrand()));
            document.add(new Paragraph("Model: " + vehicle.getModel()));
            document.add(new Paragraph("Year: " + vehicle.getYear()));
            document.add(new Paragraph("Color: " + vehicle.getColor()));

            document.add(new Paragraph("\nWork Order Details"));
            document.add(new Paragraph("--------------------------------"));
            document.add(new Paragraph("Work Order ID: " + workOrder.getId()));
            document.add(new Paragraph("Description: " + (workOrder.getDescription() != null ? workOrder.getDescription() : "N/A")));
            document.add(new Paragraph("Status: " + workOrder.getStatus()));
            document.add(new Paragraph("Start Date: " + workOrder.getStartDate()));
            document.add(new Paragraph("End Date: " + (workOrder.getEndDate() != null ? workOrder.getEndDate() : "N/A")));

            document.add(new Paragraph("\nItems Used"));
            document.add(new Paragraph("--------------------------------"));
            if (workOrder.getItems() != null && !workOrder.getItems().isEmpty()) {
                for (WorkOrderItem item : workOrder.getItems()) {
                    InventoryItem inventoryItem = inventoryItemRepository.findById(item.getInventoryItem().getId())
                            .orElse(null); // Handle case where inventory item might not be found
                    String itemName = inventoryItem != null ? inventoryItem.getName() : "Unknown Item";
                    BigDecimal itemTotal = BigDecimal.valueOf(item.getQuantity()).multiply(item.getPrice());
                    document.add(new Paragraph("  - " + itemName + ": " + item.getQuantity() + " x " + NumberFormat.getCurrencyInstance(new Locale("es", "ES")).format(item.getPrice()) + " = " + NumberFormat.getCurrencyInstance(new Locale("es", "ES")).format(itemTotal)));
                }
            } else {
                document.add(new Paragraph("  No items used."));
            }

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF for invoice " + id, e);
        }
    }

    private InvoiceDTO mapToDTO(Invoice entity) {
        InvoiceDTO dto = new InvoiceDTO();
        dto.setId(entity.getId());
        dto.setWorkOrderId(entity.getWorkOrder().getId());
        dto.setIssueDate(entity.getIssueDate());
        dto.setTotal(entity.getTotal());
        dto.setPaidAmount(entity.getPaidAmount());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setPayments(entity.getPayments().stream()
                .map(this::mapToPaymentDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    private PaymentDTO mapToPaymentDTO(Payment entity) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(entity.getId());
        dto.setInvoiceId(entity.getInvoice().getId());
        dto.setAmount(entity.getAmount());
        dto.setPaymentDate(entity.getPaymentDate());
        dto.setMethod(entity.getMethod());
        dto.setNote(entity.getNote());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
} 