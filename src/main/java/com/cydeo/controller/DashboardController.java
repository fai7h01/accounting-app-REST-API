package com.cydeo.controller;

import com.cydeo.dto.common.CurrencyDto;
import com.cydeo.dto.common.ResponseWrapper;
import com.cydeo.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/usdExchangeRates")
    public ResponseEntity<ResponseWrapper> getDashboard(){
        CurrencyDto currencyDto = dashboardService.listUsdExchangeRate();
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Usd exchange rates is successfully retrieved.")
                .data(currencyDto).build());
    }

}
