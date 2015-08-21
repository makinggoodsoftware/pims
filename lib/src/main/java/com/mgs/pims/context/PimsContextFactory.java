package com.mgs.pims.context;

import com.mgs.pims.core.linker.PimsLinker;
import com.mgs.pims.core.metaData.MetaDataFactory;
import com.mgs.pims.types.map.PimsMapEntity;
import com.mgs.reflections.ParsedType;
import com.mgs.reflections.TypeParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Optional.empty;

public class PimsContextFactory {
    private final TypeParser typeParser;
    private final MetaDataFactory metaDataFactory;
    private final PimsLinker pimsLinker;

    public PimsContextFactory(
            TypeParser typeParser,
            MetaDataFactory metaDataFactory, PimsLinker pimsLinker) {
        this.typeParser = typeParser;
        this.metaDataFactory = metaDataFactory;
        this.pimsLinker = pimsLinker;
    }

    public PimsContext create(
            List<Class<? extends PimsMapEntity>> entities
    ) {
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
                pimsLinker.link(entityClass),
                empty(),
                empty(),
                empty()
        );
    }


}
