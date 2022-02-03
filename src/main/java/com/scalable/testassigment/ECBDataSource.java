package com.scalable.testassigment;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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

    ECBDataSource(){
        euroBasedRatesMap = new HashMap<>();
        currenciesUsagesMap = new HashMap<>();

        //TODO: if initial rates download failed, exit with error and close app
        initialiseRatesMap();
    }

    //TODO: make URL configurable
    private static final String RATES_URL = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";

    private static final Logger LOGGER = LoggerFactory.getLogger(ECBDataSource.class);

    public Double getEuroBasedRate(String currency){
        //TODO: better count those things as metrics
        currenciesUsagesMap.put(currency, currenciesUsagesMap.get(currency) + 1);
        return euroBasedRatesMap.get(currency);
    }


    //downloads rates from ECB and parses gotten XML to fill the map
    public void initialiseRatesMap(){
        Document doc = downloadRates();
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

    private Document downloadRates(){
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbf.newDocumentBuilder();

            URL url = new URL(RATES_URL);
            InputStream stream = url.openStream();

            //TODO: add check that downloaded document is not empty
            Document doc = docBuilder.parse(stream);
            return doc;
        } catch(Exception ex){
            LOGGER.warn("Failed due to " + ex.getMessage());
            return null;
        }
    }

}
