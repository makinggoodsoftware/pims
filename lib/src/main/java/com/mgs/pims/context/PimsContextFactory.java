package com.mgs.pims.context;

import com.mgs.pims.core.linker.PimsLinker;
import com.mgs.pims.core.metaData.MetaDataFactory;
import com.mgs.pims.types.map.PimsMapEntity;
import com.mgs.reflections.ParsedType;
import com.mgs.reflections.TypeParser;

import java.util.*;

public class PimsContextFactory {
    private final TypeParser typeParser;
    private final MetaDataFactory metaDataFactory;
    private final PimsLinker pimsLinker;

    public PimsContextFactory(
            TypeParser typeParser,
            MetaDataFactory metaDataFactory,
            PimsLinker pimsLinker) {
        this.typeParser = typeParser;
        this.metaDataFactory = metaDataFactory;
        this.pimsLinker = pimsLinker;
    }

    public PimsContext context(
            List<PimsEntityRelationshipDescriptor> relationshipDescriptors,
            List<Class<? extends PimsMapEntity>> entities
    ) {
        Map<String, PimsEntityStaticDescriptor> staticDescriptors = new HashMap<>();
        for (Class<? extends PimsMapEntity> entity : entities) {
            PimsEntityStaticDescriptor entityDescriptor = processEntity(entity);
            staticDescriptors.put(
                    entity.getSimpleName(),
                    entityDescriptor
            );
        }
        Map<String, PimsEntityDescriptor> descriptors = new HashMap<>();
        for (Map.Entry<String, PimsEntityStaticDescriptor> staticDescriptorEntry : staticDescriptors.entrySet()) {
            PimsEntityStaticDescriptor staticDescriptor = staticDescriptorEntry.getValue();
            PimsEntityRelationships pimsEntityRelationships = relationship(relationshipDescriptors, staticDescriptor, staticDescriptors.values());
            descriptors.put(
                    staticDescriptorEntry.getKey(),
                    new PimsEntityDescriptor(
                        staticDescriptor,
                        pimsEntityRelationships
                    )
            );
        }
        return new PimsContext(descriptors);
    }

    private PimsEntityStaticDescriptor processEntity(Class<? extends PimsMapEntity> entityClass) {
        ParsedType entityType = typeParser.parse(entityClass);
        return new PimsEntityStaticDescriptor(
                entityType,
                metaDataFactory.metadata(entityType),
                pimsLinker.link(entityClass)
        );
    }

    private PimsEntityRelationships relationship(
            List<PimsEntityRelationshipDescriptor> relationshipDescriptors,
            PimsEntityStaticDescriptor pimsEntityStaticDescriptor,
            Collection<PimsEntityStaticDescriptor> candidates
    ) {
        Map<PimsEntityRelationshipDescriptor, Optional<PimsEntityStaticDescriptor>> relationships = new HashMap<>();
        for (PimsEntityRelationshipDescriptor relationshipDescriptor : relationshipDescriptors) {
            relationships.put(
                    relationshipDescriptor,
                    relationshipDescriptor.extract(
                            pimsEntityStaticDescriptor,
                            candidates
                    )
            );
        }
        return new PimsEntityRelationships(relationships);
    }
}
