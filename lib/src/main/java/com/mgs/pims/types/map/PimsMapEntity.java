package com.mgs.pims.types.map;

import com.mgs.maps.VirtualField;
import com.mgs.pims.annotations.PimsEntity;
import com.mgs.pims.types.base.PimsBaseEntity;
import com.mgs.pims.types.metaData.PimsEntityMetaData;
import com.mgs.reflections.ParsedType;

import java.util.Map;

@PimsEntity(managedBy = PimsMapEntities.class)
public interface PimsMapEntity extends PimsBaseEntity{
    @VirtualField
    Map<String, Object> getDomainMap();

    @VirtualField
    boolean isMutable();

    @VirtualField
    ParsedType getType();

    @VirtualField
    PimsEntityMetaData getMetaData();

    @Override
    String toString();

    @Override
    boolean equals (Object other);
}
