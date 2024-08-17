package com.cydeo.controller;

import com.cydeo.dto.InvoiceDto;
import com.cydeo.dto.common.CurrencyDto;
import com.cydeo.dto.common.ResponseWrapper;
import com.cydeo.service.DashboardService;
import com.cydeo.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final InvoiceService invoiceService;

    @GetMapping("/usdExchangeRates")
    public ResponseEntity<ResponseWrapper> getUsdExchangeRatesForDashboard() {
        CurrencyDto currencyDto = dashboardService.listUsdExchangeRate();
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Usd exchange rates is successfully retrieved.")
                .data(currencyDto).build());
    }

    @GetMapping("/summaryNumbers")
    public ResponseEntity<ResponseWrapper> getSummaryNumbersForDashboard() {
        Map<String, BigDecimal> summaryNumbers = dashboardService.getSummaryNumbers();
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Summary numbers data is successfully retrieved.")
                .data(summaryNumbers).build());
    }

    @GetMapping("/lastThreeApproved")
    public ResponseEntity<ResponseWrapper> getLastThreeApprovedInvoicesForDashboard() {
        List<InvoiceDto> invoices = invoiceService.listLastThreeApproved();
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Last three approved invoices is successfully retrieved.")
                .data(invoices).build());
    }
}
