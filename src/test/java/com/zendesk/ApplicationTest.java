package com.zendesk;

import com.zendesk.common.Errors;
import com.zendesk.contract.GetRouteResponse;
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

//    @Test
//    public void testService_isActive() {
//        Assert.assertEquals("MRT Router is active",this.restTemplate
//                .getForObject("http://localhost:"+port+"/isActive", String.class));
//    }
//
//    @Test
//    public void testService_InvalidSation() {
//        GetRouteResponse response = this.restTemplate
//                .getForObject("http://localhost:"+port+"/route?from=Holland&to=Bugis", GetRouteResponse.class);
//        Assert.assertNotNull(response);
//        Assert.assertNotNull(response.getError());
//        Assert.assertNull(response.getRoute());
//        Assert.assertEquals(Errors.STATION_NOT_FOUND.getCode(), response.getError().getCode());
//    }
//
//    @Test
//    public void testService_InvalidDateFormat() {
//        GetRouteResponse response = this.restTemplate
//                .getForObject("http://localhost:"+port+"/route?from=Holland&to=Bugis&date=09/Sep/1990", GetRouteResponse.class);
//        Assert.assertNotNull(response);
//        Assert.assertNotNull(response.getError());
//        Assert.assertNull(response.getRoute());
//        Assert.assertEquals(Errors.INVALID_DATE_FORMAT.getCode(), response.getError().getCode());
//    }

    @Test
    public void testService_ValidPath() {
        String response = this.restTemplate
                .getForObject("http://localhost:"+port+"/route?from=Holland Village&to=Bugis", String.class);
        System.out.println(response);
//        Assert.assertNotNull(response);
//        Assert.assertNull(response.getError());
//        Assert.assertNotNull(response.getRoute());
    }
}
