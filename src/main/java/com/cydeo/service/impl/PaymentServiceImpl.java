package com.cydeo.service.impl;

import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.PaymentDto;
import com.cydeo.entity.Company;
import com.cydeo.entity.Payment;
import com.cydeo.enums.Currency;
import com.cydeo.enums.Months;
import com.cydeo.repository.PaymentRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.PaymentService;
import com.cydeo.util.MapperUtil;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${stripe_secret_key}")
    private String stripeSecretKey;

    private final PaymentRepository paymentRepository;
    private final CompanyService companyService;
    private final MapperUtil mapperUtil;

    @PostConstruct
    public void init(){
        Stripe.apiKey = stripeSecretKey;
    }

    public PaymentServiceImpl(PaymentRepository paymentRepository, CompanyService companyService, MapperUtil mapperUtil) {
        this.paymentRepository = paymentRepository;
        this.companyService = companyService;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public List<PaymentDto> listAllByYear(int year) {
        CompanyDto company = companyService.getCompanyDtoByLoggedInUser();
        List<Payment> payments = paymentRepository.findAllByYearAndCompanyId(year, company.getId());
        if (payments.isEmpty()){
            createPaymentsForSelectedYear(year, company);
        }
        return paymentRepository.findAllByYearAndCompanyId(year, company.getId()).stream()
                .map(payment -> mapperUtil.convert(payment, new PaymentDto()))
                .sorted(Comparator.comparing(paymentDto -> paymentDto.getMonth().getId()))
                .toList();
    }

    @Override
    public PaymentDto findById(Long id) {
        Optional<Payment> foundPayment = paymentRepository.findById(id);
        if (foundPayment.isPresent()) {
            return mapperUtil.convert(foundPayment.get(), new PaymentDto());
        }
        throw new NoSuchElementException("Payment not found");
    }

    @Override
    public void createPaymentsForSelectedYear(int year, CompanyDto companyDto) {
        for (int i = 1; i <= 12; i++) {
            Payment payment = new Payment();
            payment.setYear(year);
            payment.setMonth(Months.values()[i - 1]);
            payment.setPaid(false);
            payment.setCompany(mapperUtil.convert(companyDto, new Company()));
            payment.setAmount(BigDecimal.valueOf(250));
            paymentRepository.save(payment);
        }

    }

    @Override
    public PaymentIntent createPaymentIntent(Long amount, Long id) throws StripeException {

        Payment payment = paymentRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Payment not found"));

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(amount)
                        .setCurrency(Currency.USD.name())
                        .setDescription("Cydeo accounting subscription fee for : " + payment.getYear() + " " + payment.getMonth().getValue())
                        .build();

        payment.setPaid(true);
        paymentRepository.save(payment);

        return PaymentIntent.create(params);
    }


}
