package com.scalable.testassigment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExchangeRateServiceController {

    @Autowired
    ECBDataSource dataSource;

    @GetMapping("exchangeRateService/v1/health")
    public String HealthCheck(){
        return "alive";
    }

    @GetMapping("exchangeRateService/v1/getEuroBasedRate")
    public Double getEuroBasedExchangeRate(@RequestParam(value = "currency") String currency) {
        return dataSource.getEuroBasedRate(currency);
    }
}
