package com.mgs.pims.core;

import com.mgs.pims.context.PimsEntityRelationshipDescriptor;
import com.mgs.pims.types.map.PimsMapEntity;

import java.util.List;

public interface PimsRawDataProvider {
    List<PimsEntityRelationshipDescriptor> relationshipDescriptors();

    List<Class<? extends PimsMapEntity>> entities();
}
