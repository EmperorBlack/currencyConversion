package com.microservice.currencyConversion;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "currency-exchange")
public interface CurrencyConversionProxy {

    @GetMapping(path = "/currency-exchange/from/{from}/to/{to}")
    public CurrencyConversion retriveExchangeValue(
            @PathVariable String from,
            @PathVariable String to
    );

}
