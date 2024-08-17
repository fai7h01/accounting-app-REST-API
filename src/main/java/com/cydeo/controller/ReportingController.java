package com.cydeo.controller;

import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.dto.common.ResponseWrapper;
import com.cydeo.service.InvoiceProductService;
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
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportingController {

    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;


    @GetMapping("/stockData")
    public ResponseEntity<ResponseWrapper> showStockReports(){
        List<InvoiceProductDto> invoiceProducts = invoiceProductService.findAllApprovedInvoiceProducts();
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("All approved invoice products data is successfully retrieved.")
                .data(invoiceProducts).build());
    }

    @GetMapping("/profitLossData")
    public ResponseEntity<ResponseWrapper> showProfitLossReport(){
        Map<String, BigDecimal> monthlyProfitLossData = invoiceService.getMonthlyProfitLossMap();
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Monthly profit/loss data is successfully retrieved.")
                .data(monthlyProfitLossData).build());
    }

}
