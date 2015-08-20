package com.mgs.pims.types.base;

import com.google.common.base.MoreObjects;
import com.mgs.pims.annotations.PimsMethod;
import com.mgs.pims.annotations.PimsMixer;
import com.mgs.pims.annotations.PimsParameter;
import com.mgs.pims.types.metaData.PimsEntityMetaData;
import com.mgs.reflections.ParsedType;

import java.util.Map;

import static com.mgs.pims.core.linker.parameters.PimsMethodParameterType.DOMAIN_MAP;
import static com.mgs.pims.core.linker.parameters.PimsMethodParameterType.META_DATA;
import static com.mgs.pims.core.linker.parameters.PimsMethodParameterType.SOURCE_TYPE;

@PimsMixer
public class PimsBaseEntities {
    @PimsMethod(pattern = "toString")
    public Object onToString(
            @PimsParameter(type = DOMAIN_MAP) Map<String, Object> domainMap,
            @PimsParameter(type = SOURCE_TYPE) ParsedType type
    ) {
        return MoreObjects.toStringHelper(type.getActualType().get()).
                add("domainMap", domainMap).
                toString();
    }

    @PimsMethod(pattern = "getMetaData")
    public Object onGetMetaData(
            @PimsParameter(type = META_DATA) PimsEntityMetaData metaData
    ) {
        return metaData;
    }
}
