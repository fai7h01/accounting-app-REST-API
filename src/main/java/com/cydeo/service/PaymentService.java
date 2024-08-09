package com.cydeo.service;

import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.PaymentDto;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import java.util.List;

public interface PaymentService {

    List<PaymentDto> listAllByYear(int year);
    PaymentDto findById(Long id);
    void createPaymentsForSelectedYear(int year, CompanyDto companyDto);
    PaymentIntent createPaymentIntent(Long amount, Long id) throws StripeException;
}
