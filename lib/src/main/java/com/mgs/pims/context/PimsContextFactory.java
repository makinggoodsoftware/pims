package com.mgs.pims.context;

import com.mgs.pims.core.metaData.MetaDataFactory;
import com.mgs.pims.types.map.PimsMapEntity;
import com.mgs.reflections.ParsedType;
import com.mgs.reflections.TypeParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PimsContextFactory {
    private final TypeParser typeParser;
    private final MetaDataFactory metaDataFactory;

    public PimsContextFactory(
            TypeParser typeParser,
            MetaDataFactory metaDataFactory) {
        this.typeParser = typeParser;
        this.metaDataFactory = metaDataFactory;
    }

    public PimsContext create (List<Class<? extends PimsMapEntity>> entities) {
        Map<String, PimsEntityDescriptor> descriptors = new HashMap<>();
        for (Class<? extends PimsMapEntity> entity : entities) {
            descriptors.put(
                    entity.getSimpleName(),
                    processEntity(entity)
            );
        }
        return new PimsContext(descriptors);
    }

    private PimsEntityDescriptor processEntity(Class<? extends PimsMapEntity> entityClass) {
        ParsedType entityType = typeParser.parse(entityClass);
        return new PimsEntityDescriptor(
                entityType,
                metaDataFactory.metadata(entityType),
                null,
                null,
                null,
                null
        );
    }


}
