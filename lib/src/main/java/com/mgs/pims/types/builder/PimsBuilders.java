package com.mgs.pims.types.builder;

import com.mgs.pims.annotations.PimsMethod;
import com.mgs.pims.annotations.PimsMixer;
import com.mgs.pims.annotations.PimsParameter;
import com.mgs.pims.types.PimsFactory;
import com.mgs.pims.types.map.PimsMapEntity;
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
            @PimsParameter(type = SOURCE_OBJECT) PimsMapEntity entity,
            @PimsParameter(type = DOMAIN_MAP) Map<String, Object> domainMap,
            @PimsParameter(type = VALUE_MAP) Map<String, Object> valueMap,
            @PimsParameter(type = PLACEHOLDER, name = "fieldName") String fieldName,
            @PimsParameter(type = METHOD_PARAMETERS) Object value
    ) {
        if (PimsMapEntity.class.isAssignableFrom(value.getClass())){
            PimsMapEntity castedValue = (PimsMapEntity) value;
            valueMap.put(fieldName, castedValue.getValueMap());
        } else {
            valueMap.put(fieldName, value);
        }
        domainMap.put(fieldName, value);
        return entity;
    }

    @PimsMethod(pattern = "build")
    public Object onBuild(
            @PimsParameter(type = SOURCE_TYPE) ParsedType sourceType,
            @PimsParameter(type = DOMAIN_MAP) Map<String, Object> domainMap,
            @PimsParameter(type = VALUE_MAP) Map<String, Object> valueMap
    ) {
        Declaration pimsBuilderDeclaration = sourceType.getSuperDeclarations().get(PimsBuilder.class).getOwnDeclaration();
        Declaration typeOfBuilder = pimsBuilderDeclaration.getParameters().get("T");
        return pimsFactory.immutable(typeOfBuilder.getActualType().get(), valueMap, domainMap);
    }

}
