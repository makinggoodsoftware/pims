package com.mgs.pims.types.serializable;

import com.mgs.pims.annotations.PimsMethod;
import com.mgs.pims.annotations.PimsMixer;
import com.mgs.pims.annotations.PimsParameter;

import java.util.Map;

import static com.mgs.pims.core.linker.parameters.PimsMethodParameterType.VALUE_MAP;

@PimsMixer
public class PimsSerializables {
    @PimsMethod(pattern = "getValueMap")
    public Map<String, Object> onGetValueMap (
            @PimsParameter(type = VALUE_MAP) Map<String, Object> valueMap
    ) {
        return valueMap;
    }
}
