package com.mgs.pims.context;

import com.mgs.pims.types.builder.PimsBuilder;
import com.mgs.pims.types.provider.PimsProvider;
import com.mgs.pims.types.retriever.PimsRetriever;

import java.util.Map;
import java.util.Optional;

public class PimsEntityRelationships {
    private final Map<Class, Optional<PimsEntityStaticDescriptor>> relationships;

    public PimsEntityRelationships(Map<Class, Optional<PimsEntityStaticDescriptor>> relationships) {
        this.relationships = relationships;
    }

    public Optional<PimsEntityStaticDescriptor> getBuilder() {
        return relationships.get(PimsBuilder.class);
    }

    public Optional<PimsEntityStaticDescriptor> getProvider() {
        return relationships.get(PimsProvider.class);
    }

    public Optional<PimsEntityStaticDescriptor> getRetriever() {
        return relationships.get(PimsRetriever.class);
    }
}
