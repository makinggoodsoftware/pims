package com.mgs.pims.types.serializable;

import com.mgs.maps.VirtualField;
import com.mgs.pims.annotations.PimsEntity;
import com.mgs.pims.types.map.PimsMapEntity;

import java.util.Map;

@PimsEntity(managedBy = PimsSerializables.class)
public interface PimsSerializable extends PimsMapEntity{
    @VirtualField
    Map<String, Object> getValueMap();
}
