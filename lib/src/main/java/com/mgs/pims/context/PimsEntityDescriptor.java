package com.mgs.pims.context;

import com.mgs.pims.core.linker.method.PimsMethodDelegator;
import com.mgs.pims.types.builder.PimsBuilder;
import com.mgs.pims.types.metaData.PimsEntityMetaData;
import com.mgs.pims.types.provider.PimsProvider;
import com.mgs.pims.types.retriever.PimsRetriever;
import com.mgs.reflections.ParsedType;
import com.mgs.reflections.TypelessMethod;

import java.util.Map;
import java.util.Optional;

public class PimsEntityDescriptor {
    private final ParsedType type;
    private final PimsEntityMetaData metadata;
    private final Map<TypelessMethod, PimsMethodDelegator> links;
    private final Optional<PimsBuilder> builder;
    private final Optional<PimsProvider> provider;
    private final Optional<PimsRetriever> retriever;

    public PimsEntityDescriptor(ParsedType type, PimsEntityMetaData metadata, Map<TypelessMethod, PimsMethodDelegator> links, Optional<PimsBuilder> builder, Optional<PimsProvider> provider, Optional<PimsRetriever> retriever) {
        this.type = type;
        this.metadata = metadata;
        this.links = links;
        this.builder = builder;
        this.provider = provider;
        this.retriever = retriever;
    }

    public ParsedType getType() {
        return type;
    }

    public PimsEntityMetaData getMetaData() {
        return metadata;
    }


    public Map<TypelessMethod, PimsMethodDelegator> getLinks() {
        return links;
    }

    public Optional<PimsBuilder> getBuilder() {
        return builder;
    }

    public Optional<PimsProvider> getProvider() {
        return provider;
    }

    public Optional<PimsRetriever> getRetriever() {
        return retriever;
    }
}
