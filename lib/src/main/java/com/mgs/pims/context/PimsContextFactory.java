package com.mgs.pims.context;

import com.mgs.pims.core.linker.PimsLinker;
import com.mgs.pims.core.metaData.MetaDataFactory;
import com.mgs.pims.types.map.PimsMapEntity;
import com.mgs.reflections.ParsedType;
import com.mgs.reflections.TypeParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        List<PimsEntityStaticDescriptor> builders = new ArrayList<>();
        Map<String, PimsEntityStaticDescriptor> staticDescriptors = new HashMap<>();
        for (Class<? extends PimsMapEntity> entity : entities) {
            PimsEntityStaticDescriptor entityDescriptor = processEntity(entity);
            if (isBuilder(entityDescriptor)) {
                builders.add(entityDescriptor);
            }
            staticDescriptors.put(
                    entity.getSimpleName(),
                    entityDescriptor
            );
        }

        Map<Class, List<PimsEntityStaticDescriptor>> collect = builders.stream().collect(Collectors.groupingBy(builder -> builder.getType().getActualType().get()));
        PimsEntityDescriptorFactory pimsEntityDescriptorFactory =

        Map<String, PimsEntityDescriptor> descriptors = new HashMap<>();
        for (Map.Entry<String, PimsEntityStaticDescriptor> staticDescriptor : staticDescriptors.entrySet()) {
            descriptors.put(staticDescriptor.getKey(), descriptor(staticDescriptor.getValue(), builders));
        }
        return new PimsContext(descriptors);
    }

    private boolean isBuilder(PimsEntityStaticDescriptor entityDescriptor) {
        return false;
    }

    private PimsEntityStaticDescriptor processEntity(Class<? extends PimsMapEntity> entityClass) {
        ParsedType entityType = typeParser.parse(entityClass);
        return new PimsEntityStaticDescriptor(
                entityType,
                metaDataFactory.metadata(entityType),
                pimsLinker.link(entityClass)
        );
    }


}
