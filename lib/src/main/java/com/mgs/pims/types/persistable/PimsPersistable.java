package com.mgs.pims.types.persistable;

import com.mgs.maps.Mapping;
import com.mgs.pims.annotations.PimsEntity;
import com.mgs.pims.types.map.PimsMapEntity;
import com.mgs.pims.types.serializable.PimsSerializable;

@PimsEntity(managedBy = PimsPersistables.class)
public interface PimsPersistable<T extends PimsSerializable> extends PimsSerializable {
    T getData();

    @Mapping(mapFieldName = "_id")
    String getId();

    Integer getVersion();

    PimsPersistable<T> persist ();
}
