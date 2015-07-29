package com.mgs.pims.types.entity;

import com.mgs.maps.VirtualField;
import com.mgs.pims.annotations.PimsEntity;

import java.util.Map;

@PimsEntity(managedBy = PimsMapEntities.class)
public interface PimsMapEntity {
    @VirtualField
    Map<String, Object> getValueMap();

    @VirtualField
    Map<String, Object> getDomainMap();

    @VirtualField
    boolean isMutable();

    @VirtualField
    Class<? extends PimsMapEntity> getType();
}
