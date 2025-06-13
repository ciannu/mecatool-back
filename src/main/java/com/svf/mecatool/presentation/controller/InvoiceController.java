package com.svf.mecatool.presentation.controller;

import com.svf.mecatool.business.services.InvoiceService;
import com.svf.mecatool.integration.model.InvoiceStatus;
import com.svf.mecatool.presentation.dto.InvoiceDTO;
import com.svf.mecatool.presentation.dto.PaymentDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoices")
@CrossOrigin
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("/from-work-order/{workOrderId}")
    public ResponseEntity<InvoiceDTO> createFromWorkOrder(@PathVariable Long workOrderId) {
        try {
            InvoiceDTO invoice = invoiceService.createFromWorkOrder(workOrderId);
            return ResponseEntity.ok(invoice);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDTO> getById(@PathVariable Long id) {
        try {
            InvoiceDTO invoice = invoiceService.getById(id);
            return ResponseEntity.ok(invoice);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public List<InvoiceDTO> getAll() {
        return invoiceService.getAll();
    }

    @GetMapping("/status/{status}")
    public List<InvoiceDTO> getByStatus(@PathVariable InvoiceStatus status) {
        return invoiceService.getByStatus(status);
    }

    @GetMapping("/work-order/{workOrderId}")
    public List<InvoiceDTO> getByWorkOrder(@PathVariable Long workOrderId) {
        return invoiceService.getByWorkOrder(workOrderId);
    }

    @PostMapping("/{id}/payments")
    public ResponseEntity<PaymentDTO> addPayment(
            @PathVariable Long id,
            @Valid @RequestBody PaymentDTO paymentDTO
    ) {
        try {
            PaymentDTO payment = invoiceService.addPayment(id, paymentDTO);
            return ResponseEntity.ok(payment);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> getPdf(@PathVariable Long id) {
        try {
            byte[] pdfContent = invoiceService.generatePdf(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice-" + id + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfContent);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 