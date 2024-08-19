package com.cydeo.controller;

import com.cydeo.dto.PaymentDto;
import com.cydeo.dto.common.PaymentRequestDto;
import com.cydeo.dto.common.ResponseWrapper;
import com.cydeo.service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/list")
    public ResponseEntity<ResponseWrapper> listAllPayments(@RequestParam(value = "year", required = false) String year){
        int chosenYear = year == null || year.isEmpty() ? LocalDate.now().getYear() : Integer.parseInt(year);
        List<PaymentDto> payments = paymentService.listAllByYear(chosenYear);
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Payment list is successfully retrieved.")
                .data(payments).build());
    }

    @PostMapping("/chargePayment/{id}")
    public ResponseEntity<ResponseWrapper> chargeSubscriptionFee(@PathVariable Long id) throws StripeException {
        try {
            PaymentIntent paymentIntent = paymentService.createPaymentIntent(25000L, id);
            Map<String, String> responseData = new HashMap<>();
            responseData.put("clientSecret", paymentIntent.getClientSecret());
            responseData.put("description", paymentIntent.getDescription());
            return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                    .success(true)
                    .message("Subscription fee is paid successfully.")
                    .data(responseData).build());
        }catch (StripeException e){
            return ResponseEntity.badRequest().body(null);
        }
    }
}
