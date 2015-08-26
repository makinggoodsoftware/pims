package com.mgs.pims.core;

import com.mgs.pims.context.PimsEntityRelationshipDescriptor;
import com.mgs.pims.types.map.PimsMapEntity;

import java.util.List;

public class HardcodedPimsRawDataProvider implements PimsRawDataProvider {
    private final List<PimsEntityRelationshipDescriptor> relationshipDescriptors;
    private final List<Class<? extends PimsMapEntity>> entities;

    public HardcodedPimsRawDataProvider(List<PimsEntityRelationshipDescriptor> relationshipDescriptors, List<Class<? extends PimsMapEntity>> entities) {
        this.relationshipDescriptors = relationshipDescriptors;
        this.entities = entities;
    }

    @Override
    public List<PimsEntityRelationshipDescriptor> relationshipDescriptors() {
        return relationshipDescriptors;
    }

    @Override
    public List<Class<? extends PimsMapEntity>> entities() {
        return entities;
    }
}
