package com.sribalajipg.controller;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.sribalajipg.entity.Payment;
import com.sribalajipg.entity.PaymentStatus;
import com.sribalajipg.repository.PaymentRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentRepository paymentRepository;

    @Value("${razorpay.key-id}")
    private String keyId;
    @Value("${razorpay.key-secret}")
    private String keySecret;

    public PaymentController(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

   @GetMapping("/tenant/{tenantId}")
public List<Payment> history(@PathVariable Long tenantId) {
    return paymentRepository.findByTenant_Id(tenantId);
}

    @GetMapping("/pending")
    public List<Payment> pending() {
        return paymentRepository.findByStatus(PaymentStatus.PENDING);
    }

    // Step 1: create a Razorpay order for a due payment
    @PostMapping("/{paymentId}/create-order")
    public ResponseEntity<?> createOrder(@PathVariable Long paymentId) throws Exception {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow();

        RazorpayClient razorpay = new RazorpayClient(keyId, keySecret);
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", (int) (payment.getAmount() * 100)); // paise
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "rent_" + payment.getId());
        Order order = razorpay.orders.create(orderRequest);

        payment.setRazorpayOrderId(order.get("id"));
        paymentRepository.save(payment);

        return ResponseEntity.ok(Map.of("orderId", order.get("id"), "amount", payment.getAmount(), "keyId", keyId));
    }

    // Step 2: frontend confirms payment success after Razorpay checkout callback.
    // In production, ALSO verify via Razorpay webhook + signature before trusting this.
    @PostMapping("/{paymentId}/confirm")
    public ResponseEntity<?> confirmPayment(@PathVariable Long paymentId, @RequestBody Map<String, String> body) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow();
        payment.setRazorpayPaymentId(body.get("razorpay_payment_id"));
        payment.setPaymentMethod(body.getOrDefault("method", "Razorpay"));
        payment.setStatus(PaymentStatus.PAID);
        payment.setPaidDate(LocalDate.now());
        // TODO: generate invoice/receipt PDF, upload to Cloudinary, set invoiceUrl/receiptUrl,
        // then trigger SMS/WhatsApp/email/push notification for "Payment Success".
        paymentRepository.save(payment);
        return ResponseEntity.ok(payment);
    }
}
