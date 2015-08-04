package com.mgs.pims.types.persistable;

import com.mgs.pims.annotations.PimsMethod;
import com.mgs.pims.annotations.PimsMixer;
import com.mgs.pims.annotations.PimsParameter;
import com.mgs.pims.types.entity.PimsMapEntity;

import static com.mgs.pims.linker.parameters.PimsMethodParameterType.SOURCE_OBJECT;

@PimsMixer
public class PimsPersistables {
    @PimsMethod(pattern = "persist")
    public <T extends PimsMapEntity> Object onPersist(
            @PimsParameter(type = SOURCE_OBJECT) PimsPersistable<T> pimsPersistable
    ) {
        return pimsPersistable;
    }
}
