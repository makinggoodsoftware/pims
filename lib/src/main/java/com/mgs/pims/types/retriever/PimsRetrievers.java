package com.mgs.pims.types.retriever;

import com.mgs.pims.annotations.PimsMethod;
import com.mgs.pims.annotations.PimsMixer;
import com.mgs.pims.annotations.PimsParameter;
import com.mgs.pims.types.map.PimsMapEntity;
import com.mgs.reflections.ParsedType;

import java.util.List;

import static com.mgs.pims.linker.parameters.PimsMethodParameterType.PLACEHOLDER;
import static com.mgs.pims.linker.parameters.PimsMethodParameterType.SOURCE_TYPE;

@PimsMixer
public class PimsRetrievers {
    @PimsMethod(pattern = "by{fieldName}")
    public <T extends PimsMapEntity> List<T> onByFieldName (
            @PimsParameter(type = SOURCE_TYPE)ParsedType type,
            @PimsParameter(type = PLACEHOLDER, name = "fieldName") String fieldName
    ) {
        return null;
    }
}
