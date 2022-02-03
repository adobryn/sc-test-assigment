package com.scalable.testassigment;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

@SpringBootApplication
public class ExchangeRateService {
	@Autowired
	ECBDataSource dataSource;

	//TODO: make URL configurable
	private static final String RATES_URL = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
	private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeRateService.class);

	private static Document downloadRates(){
		try (final InputStream stream = new URL(RATES_URL).openStream()){
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbf.newDocumentBuilder();

			//TODO: add check that downloaded document is not empty
			Document doc = docBuilder.parse(stream);
			return doc;
		} catch(ParserConfigurationException | SAXException | IOException ex){
			LOGGER.warn("Failed due to " + ex.getMessage());
			return null;
		}
	}

	@PostConstruct
	public void initRates(){
		Document doc = downloadRates();
		if(doc != null) {
			LOGGER.info("Rates downloaded successfully");
			dataSource.initialiseRatesMap(doc);
		} else {
			LOGGER.warn("Downloading rates failed, exiting the application");
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(ExchangeRateService.class, args);
	}
}
