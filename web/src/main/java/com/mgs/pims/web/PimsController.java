package com.mgs.pims.web;

import com.mgs.pims.types.map.PimsMapEntity;
import com.mgs.pims.types.persistable.PimsPersistable;
import com.mgs.pims.types.provider.PimsProvider;
import com.mgs.pims.types.retriever.PimsRetriever;
import com.mgs.pims.types.serializable.PimsSerializable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class PimsController {
    @Resource
    private PimsContext pimsContext;

    @RequestMapping(
            value = "/{resourceName}/{fieldName}/{fieldValue}",
            method = RequestMethod.GET,
            produces = "application/json"
    )
    @ResponseBody
    <Z extends PimsSerializable, T extends PimsPersistable<Z>>
    List<Map<String, Object>> retrieveBy(
            @PathVariable("resourceName") String name,
            @PathVariable("fieldName") String fieldName,
            @PathVariable("fieldValue") String fieldValue
    ) {
        //noinspection unchecked
        PimsRetriever<Z, T> retriever = pimsContext.retriever(name);
        List<T> resources = retriever.byField(fieldName, fieldValue);
        return resources.stream().map(t -> t.getData().getDomainMap()).collect(Collectors.toList());
    }

    @ResponseBody
    <T extends PimsMapEntity>
    List<Map<String, Object>> retrieveAll(
            @PathVariable("resourceName") String name
    ) {
        //noinspection unchecked
        PimsProvider<T> retriever = pimsContext.provider(name);
        List<T> resources = retriever.get();
        return resources.stream().map(PimsMapEntity::getDomainMap).collect(Collectors.toList());
    }

}