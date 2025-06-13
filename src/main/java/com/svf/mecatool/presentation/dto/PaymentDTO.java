package com.svf.mecatool.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private Long id;
    private Long invoiceId;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private String method;
    private String note;
    private LocalDateTime createdAt;
} 