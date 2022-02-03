package com.scalable.testassigment;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExchangeRateServiceController {

    @GetMapping("exchangeRateService/v1/health")
    public String HealthCheck(){
        return "alive";
    }

    @PostMapping("exchangeRateService/v1/getEuroBasedRate")
    public String getEuroBasedExchangeRate(@RequestParam(value = "currency") String currency) {
        return "a";
    }
}
