package com.microservice.currencyConversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CurrencyConversionController {

    @Autowired
    private CurrencyConversionProxy currencyConversionProxy;

    @GetMapping(path = "currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyConversion(@PathVariable String from,
                                                          @PathVariable String to,
                                                          @PathVariable BigDecimal quantity){

        Map<String,String> uriVars = new HashMap<>();
        uriVars.put("from",from);
        uriVars.put("to",to);

        ResponseEntity<CurrencyConversion> response = new RestTemplate().getForEntity("http://localhost:9091/currency-exchange/from/{from}/to/{to}",CurrencyConversion.class,uriVars);
        CurrencyConversion currencyConversion = response.getBody();
        return new CurrencyConversion(currencyConversion.getId(),from,to,
                currencyConversion.getConversionMultiple(),
        quantity,
        quantity.multiply(currencyConversion.getConversionMultiple())
        ,currencyConversion.getEnv());
//        return  new CurrencyConversion();
    }

    @GetMapping(path = "currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateFeignCurrencyConversion(@PathVariable String from,
                                                          @PathVariable String to,
                                                          @PathVariable BigDecimal quantity){


        CurrencyConversion currencyConversion = currencyConversionProxy.retriveExchangeValue(from,to);
        return new CurrencyConversion(currencyConversion.getId(),from,to,
                currencyConversion.getConversionMultiple(),
                quantity,
                quantity.multiply(currencyConversion.getConversionMultiple())
                ,currencyConversion.getEnv());
    }
}
