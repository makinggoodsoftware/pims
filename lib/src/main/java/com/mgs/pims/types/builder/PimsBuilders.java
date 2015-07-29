package com.mgs.pims.types.builder;

import com.mgs.pims.annotations.PimsMethod;
import com.mgs.pims.annotations.PimsMixer;
import com.mgs.pims.annotations.PimsParameter;
import com.mgs.pims.types.entity.PimsFactory;
import com.mgs.reflections.Declaration;
import com.mgs.reflections.ParsedType;

import java.util.Map;

import static com.mgs.pims.linker.parameters.PimsMethodParameterType.*;

@PimsMixer
public class PimsBuilders {
    private final PimsFactory pimsFactory;

    public PimsBuilders(PimsFactory pimsFactory) {
        this.pimsFactory = pimsFactory;
    }

    @PimsMethod(pattern = "with{fieldName}")
    public Object onWith(
            @PimsParameter(type = DOMAIN_MAP) Map<String, Object> domainMap,
            @PimsParameter(type = PLACEHOLDER, name = "fieldName") String fieldName,
            @PimsParameter(type = METHOD_PARAMETERS) Object value
    ) {
        return domainMap.put(fieldName, value);
    }

    @PimsMethod(pattern = "build")
    public Object onBuild(
            @PimsParameter(type = SOURCE_TYPE) ParsedType sourceType,
            @PimsParameter(type = DOMAIN_MAP) Map<String, Object> domainMap
    ) {
        Declaration pimsMapTypeDeclaration = sourceType.getOwnDeclaration().getParameters().get("T");
        return pimsFactory.immutable(pimsMapTypeDeclaration.getActualType().get(), domainMap);
    }

}
