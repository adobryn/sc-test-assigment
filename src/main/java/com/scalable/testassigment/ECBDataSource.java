package com.scalable.testassigment;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Component
public class ECBDataSource {
    private Map<String, Double> euroBasedRatesMap;
    private Map<String, Integer> currenciesUsagesMap;

    private static final Logger LOGGER = LoggerFactory.getLogger(ECBDataSource.class);

    //TODO: make URLs configurable
    private static final String CHART_URL_PREFIX = "https://www.ecb.europa.eu/stats/policy_and_exchange_rates"
            + "/euro_reference_exchange_rates/html/eurofxref-graph-";
    private static final String CHART_URL_SUFFIX = ".en.html";

    public Double getEuroBasedRate(String currency){
        //TODO: better count those things as metrics
        currenciesUsagesMap.put(currency, currenciesUsagesMap.get(currency) + 1);

        return euroBasedRatesMap.getOrDefault(currency, 1.0);
    }

    //TODO: do we need to round up here?
    public Double getCustomBasedRate(String currency1, String currency2){
        currenciesUsagesMap.put(currency1, currenciesUsagesMap.get(currency1) + 1);
        currenciesUsagesMap.put(currency2, currenciesUsagesMap.get(currency2) + 1);

        return euroBasedRatesMap.getOrDefault(currency1, 1.0)
                / euroBasedRatesMap.getOrDefault(currency2, 1.0);
    }

    public Map getCurrenciesUsagesMap(){
        return currenciesUsagesMap;
    }

    public String getChartUrl(String currency){
        return CHART_URL_PREFIX
                + currency.toLowerCase(Locale.ROOT) + CHART_URL_SUFFIX;
    }

    //parses XML to fill the map
    public void initialiseRatesMap(Document doc){
        euroBasedRatesMap = new HashMap<>();
        currenciesUsagesMap = new HashMap<>();

        doc.getDocumentElement().normalize();

        NodeList list = doc.getElementsByTagName("Cube");
        for(int i = 0; i < list.getLength(); i++){
            Node node = list.item(i);

            //we need only empty "cube" nodes here with currency and rate attributes
            if (node.getNodeType() == Node.ELEMENT_NODE && !node.hasChildNodes()) {
                Element element = (Element) node;
                String currency = element.getAttribute("currency");
                String rate = element.getAttribute("rate");
                LOGGER.info("Read item currency: " + currency + " rate: "  + rate);

                if(!currency.isEmpty() && !rate.isEmpty()){
                    euroBasedRatesMap.put(currency, Double.parseDouble(rate));
                    currenciesUsagesMap.put(currency, 0);
                }
            }
        }
    }
}
