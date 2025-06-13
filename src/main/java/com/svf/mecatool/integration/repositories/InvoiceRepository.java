package com.svf.mecatool.integration.repositories;

import com.svf.mecatool.integration.model.Invoice;
import com.svf.mecatool.integration.model.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByStatus(InvoiceStatus status);
    List<Invoice> findByWorkOrderId(Long workOrderId);
} 