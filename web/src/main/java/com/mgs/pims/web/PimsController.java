package com.mgs.pims.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
public class PimsController {

    @RequestMapping(value = "/{resourceName}/{resourceId}}", method = RequestMethod.GET)
    Map<String, Object> byId(
            @PathVariable("resourceName") String resourceName,
            @PathVariable("resourceId") String resourceId
    ) {
        return null;
    }
}