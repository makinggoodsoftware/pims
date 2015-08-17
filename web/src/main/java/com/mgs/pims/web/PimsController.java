package com.mgs.pims.web;

import com.mgs.pims.Pims;
import com.mgs.pims.types.retriever.PimsRetriever;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class PimsController {
    @Resource
    private PimsContext pimsContext;
    @Resource
    private Pims pims;

    @RequestMapping(
            value = "/{resourceName}/{fieldName}/{fieldValue}",
            method = RequestMethod.GET,
            produces = "application/json"
    )
    @ResponseBody
    List hello2(
            @PathVariable("resourceName") String name,
            @PathVariable("fieldName") String fieldName,
            @PathVariable("fieldValue") String fieldValue
    ) {
        PimsRetriever retriever = pimsContext.retriever(name);
        return retriever.byField(fieldName, fieldValue);
    }

}