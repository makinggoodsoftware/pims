package com.mgs.pims.web;

import com.mgs.pims.Pims;
import com.mgs.pims.types.retriever.PimsRetriever;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class PimsController {
    @Resource
    private PimsContext pimsContext;
    @Resource
    private Pims pims;

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
        PimsRetriever retriever = pimsContext.retriever(name);

        return "loading resource " + name + " with id: " + attr;
    }

}