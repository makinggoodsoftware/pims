package com.mgs.spring.glue.pims;

import com.mgs.pims.context.PimsEntityRelationshipDescriptor;
import com.mgs.pims.types.map.PimsMapEntity;

import java.util.List;

public interface PimsCustomConfig {
    List<PimsEntityRelationshipDescriptor> relationshipDescriptors();

    List<Class<? extends PimsMapEntity>> entitites();
}
