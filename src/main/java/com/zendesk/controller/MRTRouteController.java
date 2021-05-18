package com.zendesk.controller;

import org.springframework.web.bind.annotation.*;

// API mapping points
@RestController
public class MRTRouteController {

    @RequestMapping("/isActive")
    @ResponseBody
    public String isActive() throws Exception {
        return "MRT Router is active";
    }
}
