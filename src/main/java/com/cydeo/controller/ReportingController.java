package com.cydeo.controller;

import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.dto.common.response.ResponseWrapper;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Reports Controller", description = "Reports API")
public class ReportingController {

    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;


    @GetMapping("/stockData")
    @Operation(summary = "List all approved invoice products")
    public ResponseEntity<ResponseWrapper> showStockReports(){
        List<InvoiceProductDto> invoiceProducts = invoiceProductService.listAllApprovedInvoiceProducts();
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("All approved invoice products data is successfully retrieved.")
                .data(invoiceProducts).build());
    }

    @GetMapping("/profitLossData")
    @Operation(summary = "Show monthly profit/loss")
    public ResponseEntity<ResponseWrapper> showProfitLossReport(){
        Map<String, BigDecimal> monthlyProfitLossData = invoiceService.getMonthlyProfitLossMap();
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Monthly profit/loss data is successfully retrieved.")
                .data(monthlyProfitLossData).build());
    }

}
