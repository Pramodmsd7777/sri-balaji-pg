package com.sribalajipg.repository;

import com.sribalajipg.entity.Payment;
import com.sribalajipg.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByTenantId(Long tenantId);
    List<Payment> findByStatus(PaymentStatus status);
}
