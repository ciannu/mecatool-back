package com.svf.mecatool.business.services;

import com.svf.mecatool.integration.model.InvoiceStatus;
import com.svf.mecatool.presentation.dto.InvoiceDTO;
import com.svf.mecatool.presentation.dto.PaymentDTO;

import java.util.List;

public interface InvoiceService {
    InvoiceDTO createFromWorkOrder(Long workOrderId);
    InvoiceDTO getById(Long id);
    List<InvoiceDTO> getAll();
    List<InvoiceDTO> getByStatus(InvoiceStatus status);
    List<InvoiceDTO> getByWorkOrder(Long workOrderId);
    PaymentDTO addPayment(Long invoiceId, PaymentDTO paymentDTO);
    byte[] generatePdf(Long id);
} 