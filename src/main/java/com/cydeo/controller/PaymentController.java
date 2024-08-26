package com.cydeo.controller;

import com.cydeo.dto.PaymentDto;
import com.cydeo.dto.common.response.PaymentResponse;
import com.cydeo.dto.common.response.ResponseWrapper;
import com.cydeo.service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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
            PaymentResponse paymentResponse = PaymentResponse.builder().clientSecret(paymentIntent.getClientSecret())
                    .description(paymentIntent.getDescription()).build();
            return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                    .success(true)
                    .message("Subscription fee is paid successfully.")
                    .data(paymentResponse).build());
        }catch (StripeException e){
            return ResponseEntity.badRequest().body(null);
        }
    }
}
