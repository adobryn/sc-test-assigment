package com.scalable.testassigment;

import java.util.Map;

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

    //TODO: maybe it would be better to combine those two methods with default second parameter EUR?
    //depends on the spec/frontend/users
    @GetMapping("exchangeRateService/v1/getEuroBasedRate")
    public Double getEuroBasedExchangeRate(@RequestParam(value = "currency") String currency) {
        return dataSource.getEuroBasedRate(currency);
    }

    @GetMapping("exchangeRateService/v1/getCustomBasedRate")
    public Double getCustomBasedExchangeRate(@RequestParam(value = "currency1") String currency1,
            @RequestParam(value = "currency2") String currency2) {
        return dataSource.getCustomBasedRate(currency1, currency2);
    }

    @GetMapping("exchangeRateService/v1/getCurrenciesList")
    public Map getEuroBasedExchangeRate() {
        return dataSource.getCurrenciesUsagesMap();
    }

    @GetMapping("exchangeRateService/v1/getChartUrl")
    public String getInteractiveChartUrl(@RequestParam(value = "currency") String currency) {
        return dataSource.getChartUrl(currency);
    }

    @GetMapping("exchangeRateService/v1/convertCurrency")
    public Double convertCurrency(@RequestParam(value = "currency1") String currency1,
            @RequestParam(value = "amountOfCurrency1") Double amountOfCurrency1,
            @RequestParam(value = "currency2") String currency2) {
        return dataSource.convertCurrencyToAnother(currency1, amountOfCurrency1, currency2);
    }
}
