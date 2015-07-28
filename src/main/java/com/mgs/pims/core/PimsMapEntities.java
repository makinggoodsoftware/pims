package com.mgs.pims.core;

import com.mgs.pims.annotations.PimsMethod;
import com.mgs.pims.annotations.PimsMixer;
import com.mgs.pims.annotations.PimsParameter;
import com.mgs.reflections.ParsedType;

import java.util.Map;

import static com.mgs.pims.core.PimsMethodParameterType.*;

@PimsMixer
public class PimsMapEntities {
    @PimsMethod(pattern = "getDomainMap")
    public Map<String, Object> onGetDomainMap (
            @PimsParameter(type = DOMAIN_MAP) Map<String, Object> domainMap
    ) {
        return domainMap;
    }

    @PimsMethod(pattern = "getValueMap")
    public Map<String, Object> onGetValueMap (
            @PimsParameter(type = VALUE_MAP) Map<String, Object> valueMap
    ) {
        return valueMap;
    }

    @PimsMethod(pattern = "getHasPlaceholders")
    public Class<? extends PimsMapEntity> onGetType(
            @PimsParameter(type = PROXY_OBJECT) PimsEntityProxy pimsEntityProxy
    ) {
        return pimsEntityProxy.getType();
    }

    @PimsMethod(pattern = "isMutable")
    public boolean onIsMutable(
            @PimsParameter(type = PROXY_OBJECT) PimsEntityProxy pimsEntityProxy
    ) {
        return pimsEntityProxy.isMutable();
    }

    @PimsMethod(pattern = "get{fieldName}")
    public Object onGetter(
            @PimsParameter(type = DOMAIN_MAP) Map<String, Object> domainMap,
            @PimsParameter(type = PLACEHOLDER, name = "fieldName") String fieldName
    ) {
        return domainMap.get(fieldName);
    }

}