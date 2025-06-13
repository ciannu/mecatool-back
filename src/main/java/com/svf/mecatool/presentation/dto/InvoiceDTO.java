package com.svf.mecatool.presentation.dto;

import com.svf.mecatool.integration.model.InvoiceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDTO {
    private Long id;
    private Long workOrderId;
    private LocalDate issueDate;
    private BigDecimal total;
    private BigDecimal paidAmount;
    private InvoiceStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PaymentDTO> payments;
} 