package com.mgs.pims.web;

import org.springframework.web.bind.annotation.*;

@RestController
public class PimsController {

    @RequestMapping(
            value = "/{resourceName}/{resourceId}",
            method = RequestMethod.GET,
            produces = "application/json"
    )
    @ResponseBody
    String hello2(
            @PathVariable("resourceName") String name,
            @PathVariable("resourceId") String attr
    ) {
        return "loading resource " + name + " with id: " + attr;
    }

}