package com.mgs.pims.types.builder;

import com.mgs.pims.annotations.PimsEntity;
import com.mgs.pims.types.map.PimsMapEntity;

@PimsEntity(managedBy = PimsBuilders.class)
public interface PimsBuilder<T extends PimsMapEntity> extends PimsMapEntity {
    T build ();
}
