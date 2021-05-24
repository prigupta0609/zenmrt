package com.zendesk;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations="classpath:config/application.yml")
public class ApplicationTest {

    @Value("${server.port}")
    private int port;

    @Value("${server.hostname}")
    private String hostname;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testService_isActive() {
        Assert.assertEquals("MRT Router is active",this.restTemplate
                .getForObject("http://localhost:"+port+"/isActive", String.class));
    }

    @Test
    public void testService_InvalidSation() {
        String response = this.restTemplate
                .getForObject("http://localhost:"+port+"/route?from=Holland&to=Bugis", String.class);
        String expectedError = "Exception occurred : err_001 -> Origin or destination station do not exist";
        Assert.assertEquals(expectedError, response);
    }

    @Test
    public void testService_InvalidDateFormat() {
        String response = this.restTemplate
                .getForObject("http://localhost:"+port+"/route?from=Holland&to=Bugis&date=09/Sep/1990", String.class);
        String expectedError = "Exception occurred : err_002 -> Invalid date format";
        Assert.assertEquals(expectedError, response);
    }

    @Test
    public void testService_ValidPath() {
        String response = this.restTemplate
                .getForObject("http://localhost:"+port+"/route?from=Holland Village&to=Bugis", String.class);
        StringBuilder expectedResult = new StringBuilder("");
        expectedResult.append("Travel from Holland Village to Bugis\n")
                .append("Stations travelled : 8\n")
                .append("Route : ('CC21', 'CC20', 'CC19', 'DT9', 'DT10', 'DT11', 'DT12', 'DT13', 'DT14')\n\n")
                .append("Take CC line from Holland Village to Farrer Road\n")
                .append("Take CC line from Farrer Road to Botanic Gardens\n")
                .append("Change from CC line to DT line\n")
                .append("Take DT line from Botanic Gardens to Stevens\n")
                .append("Take DT line from Stevens to Newton\n")
                .append("Take DT line from Newton to Little India\n")
                .append("Take DT line from Little India to Rochor\n")
                .append("Take DT line from Rochor to Bugis\n");
        Assert.assertEquals(expectedResult.toString(), response);
    }

    @Test
    public void testService_ValidPathWithDate() {
        String response = this.restTemplate
                .getForObject("http://localhost:"+port+"/route?from=Boon Lay&to=Little India&date=13/04/2021", String.class);
        StringBuilder expectedResult = new StringBuilder("");
        expectedResult.append("Travel from Boon Lay to Little India\n")
                .append("Stations travelled : 14\n")
                .append("Route : ('EW27', 'EW26', 'EW25', 'EW24', 'EW23', 'EW22', 'EW21', 'CC22', 'CC21', 'CC20', 'CC19', 'DT9', 'DT10', 'DT11', 'DT12')\n\n")
                .append("Take EW line from Boon Lay to Lakeside\n")
                .append("Take EW line from Lakeside to Chinese Garden\n")
                .append("Take EW line from Chinese Garden to Jurong East\n")
                .append("Take EW line from Jurong East to Clementi\n")
                .append("Take EW line from Clementi to Dover\n")
                .append("Take EW line from Dover to Buona Vista\n")
                .append("Change from EW line to CC line\n")
                .append("Take CC line from Buona Vista to Holland Village\n")
                .append("Take CC line from Holland Village to Farrer Road\n")
                .append("Take CC line from Farrer Road to Botanic Gardens\n")
                .append("Change from CC line to DT line\n")
                .append("Take DT line from Botanic Gardens to Stevens\n")
                .append("Take DT line from Stevens to Newton\n")
                .append("Take DT line from Newton to Little India\n");
        Assert.assertEquals(expectedResult.toString(), response);
    }

    @Test
    public void testService_UnreachableDestination() {
        String response = this.restTemplate
                .getForObject("http://localhost:"+port+"/route?from=Holland Village&to=Unreachable Station", String.class);
        String expectedError = "Exception occurred : err_003 -> Origin and destination stations are not connected";
        Assert.assertEquals(expectedError, response);
    }
}
