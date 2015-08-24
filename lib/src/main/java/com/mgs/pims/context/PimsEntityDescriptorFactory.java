package com.mgs.pims.context;

import java.util.Map;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public class PimsEntityDescriptorFactory {
    private final Map<Class, PimsEntityStaticDescriptor> builders;
    private final Map<Class, PimsEntityStaticDescriptor> providers;
    private final Map<Class, PimsEntityStaticDescriptor> retrievers;

    public PimsEntityDescriptorFactory(
            Map<Class, PimsEntityStaticDescriptor> builders,
            Map<Class, PimsEntityStaticDescriptor> providers,
            Map<Class, PimsEntityStaticDescriptor> retrievers
    ) {
        this.builders = builders;
        this.providers = providers;
        this.retrievers = retrievers;
    }

    public PimsEntityDescriptor create(PimsEntityStaticDescriptor pimsEntityStaticDescriptor) {
        Class type = pimsEntityStaticDescriptor.getType().getActualType().get();

        PimsEntityStaticDescriptor builder = builders.get(type);
        PimsEntityStaticDescriptor provider = providers.get(type);
        PimsEntityStaticDescriptor retriever = retrievers.get(type);

        PimsEntityRelationships pimsEntityRelationships = new PimsEntityRelationships(
                builder != null ? of(builder) : empty(),
                provider != null ? of(provider) : empty(),
                retriever != null ? of(retriever) : empty()
        );
        return new PimsEntityDescriptor(pimsEntityStaticDescriptor, pimsEntityRelationships);
    }

}
