package com.mgs.pims.context;

import com.mgs.pims.core.linker.PimsLink;
import com.mgs.pims.types.metaData.PimsEntityMetaData;
import com.mgs.reflections.ParsedType;

import java.util.Optional;

public class PimsEntityDescriptor {
    private final ParsedType type;
    private final PimsEntityMetaData metadata;
    private final PimsLink links;
    private final Optional<PimsEntityDescriptor> builder;
    private final Optional<PimsEntityDescriptor> provider;
    private final Optional<PimsEntityDescriptor> retriever;

    public PimsEntityDescriptor(ParsedType type, PimsEntityMetaData metadata, PimsLink links, Optional<PimsEntityDescriptor> builder, Optional<PimsEntityDescriptor> provider, Optional<PimsEntityDescriptor> retriever) {
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


    public PimsLink getLinks() {
        return links;
    }

    public Optional<PimsEntityDescriptor> getBuilder() {
        return builder;
    }

    public Optional<PimsEntityDescriptor> getProvider() {
        return provider;
    }

    public Optional<PimsEntityDescriptor> getRetriever() {
        return retriever;
    }
}
