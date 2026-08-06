package com.sribalajipg.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.YearMonth;


@Entity
@Table(name = "payments")
@Data
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;
    private String billingMonth;
    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.PENDING;

    private String paymentMethod;      // UPI, PhonePe, GPay, Paytm, Card, NetBanking, Razorpay
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private LocalDate dueDate;
    private LocalDate paidDate;
    private String invoiceUrl;
    private String receiptUrl;
}
