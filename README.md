# Exchange Rate Service REST API

## Starting point
`localhost:8080`

## Base URI
`exchangeRateService/v1/`

## Supported services

## GET
## health
Returns "alive" if application is started

## getEuroBasedRate
Returns euro based rate value, parameter: currency

## getCustomBasedRate
Returns custom based rate value, parameters: currency1, currency2

## getCurrenciesList
Returns a list of supported currencies with an amount of usages

## getChartUrl
Returns a link to a public website showing an interactive chart for a given
currency. Parameter: currency

## convertCurrency
Converts an amount in a given currency to another. Parameters: currency1, amountOfCurrency1, currency2