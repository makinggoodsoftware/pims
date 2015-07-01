package com.mgs.pims.core;

import com.mgs.maps.VirtualField;
import com.mgs.pims.annotations.PimsEntity;
import com.mgs.reflections.ParsedType;

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
    ParsedType getType();
}
