package com.pims.core;

import com.pims.annotations.PimsEntity;
import com.reflections.ParsedType;

import java.util.Map;

@PimsEntity(managedBy = PimsMapEntities.class)
public interface PimsMapEntity {
    Map<String, Object> getValueMap();

    Map<String, Object> getDomainMap();

    boolean isMutable();

    ParsedType getType();
}
