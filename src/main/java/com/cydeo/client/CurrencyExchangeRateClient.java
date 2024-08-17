package com.cydeo.client;

import com.cydeo.dto.common.ExchangeRateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(url = "${api.currency.url}", name = "ExchangeRateCurrency-Client")
public interface CurrencyExchangeRateClient {

    @GetMapping
    ExchangeRateDto getUsdExchangeRate();

}
