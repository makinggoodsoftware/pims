package com.mgs.pims.types.persistable;

import com.mgs.pims.PimsContext;
import com.mgs.pims.annotations.PimsMethod;
import com.mgs.pims.annotations.PimsMixer;
import com.mgs.pims.annotations.PimsParameter;
import com.mgs.pims.types.map.PimsMapEntity;

import static com.mgs.pims.linker.parameters.PimsMethodParameterType.CONTEXT;
import static com.mgs.pims.linker.parameters.PimsMethodParameterType.SOURCE_OBJECT;

@PimsMixer
public class PimsPersistables {
    @PimsMethod(pattern = "persist")
    public <T extends PimsMapEntity> Object onPersist(
            @PimsParameter(type = SOURCE_OBJECT) PimsPersistable<T> pimsPersistable,
            @PimsParameter(type = CONTEXT) PimsContext pimsContext
    ) {

        return pimsPersistable;
    }
}
