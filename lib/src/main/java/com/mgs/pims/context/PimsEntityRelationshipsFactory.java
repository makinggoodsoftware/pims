package com.mgs.pims.context;

import com.mgs.pims.types.base.PimsBaseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PimsEntityRelationshipsFactory {
    private final List<Class<? extends PimsBaseEntity>> relationshipDescriptors;

    public PimsEntityRelationshipsFactory(List<PimsEntityRelationshipDescriptor> relationshipDescriptors) {
        this.relationshipDescriptors = relationshipDescriptors;
    }

    public PimsEntityRelationships create(PimsEntityStaticDescriptor pimsEntityStaticDescriptor) {
        Map<Class, Optional<PimsEntityStaticDescriptor>> relationships = new HashMap<>();
        for (Class<? extends PimsBaseEntity> relationshipDescriptor : relationshipDescriptors) {
            relationships.put(
                    relationshipDescriptor,
                    relationshipDescriptor.getValue(pimsEntityStaticDescriptor, )
            );
        }
        return new PimsEntityRelationships(relationships);
    }
}
