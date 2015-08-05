package com.mgs.pims.types.persistable;

import com.mgs.pims.annotations.PimsEntity;
import com.mgs.pims.types.map.PimsMapEntity;

@PimsEntity(managedBy = PimsPersistables.class)
public interface PimsPersistable<T extends PimsMapEntity> extends PimsMapEntity {
    T getData();

    Integer getId();

    Integer getVersion();

    PimsPersistable<T> persist ();
}
