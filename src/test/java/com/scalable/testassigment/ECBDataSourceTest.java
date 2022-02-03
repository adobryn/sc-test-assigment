package com.scalable.testassigment;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

//TODO: add edge cases
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ECBDataSourceTest {

    ECBDataSource dataSource;
    private static final String XML_DOC = "<Cube><Cube time='2022-01-31'><Cube currency='USD' rate='1.1156'/>"
            + "<Cube currency='HUF' rate='357.19'/></Cube></Cube>";

    @BeforeEach
    void setUp() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbf.newDocumentBuilder();
        Document doc = docBuilder.parse(new InputSource(new StringReader(XML_DOC)));
        dataSource = new ECBDataSource();
        dataSource.initialiseRatesMap(doc);
    }

    @Test
    void shouldGetEuroBasedRate() {
        Double rate = dataSource.getEuroBasedRate("USD");
        assertEquals(rate, 1.1156);
    }

    @Test
    void shouldGetCustomBasedRate() {
        Double rate = dataSource.getCustomBasedRate("USD", "HUF");
        assertEquals(rate, 0.003123267728659817);
    }

    @Test
    void shouldGetCurrenciesListWithCorrectOccurrences() {
        dataSource.getEuroBasedRate("USD");
        dataSource.getEuroBasedRate("USD");
        dataSource.getEuroBasedRate("HUF");

        Map result = dataSource.getCurrenciesUsagesMap();
        assertFalse(result.isEmpty());
        assertEquals(result.entrySet().size(), 2);
        assertEquals(result.get("USD"), 2);
        assertEquals(result.get("HUF"), 1);
    }
}