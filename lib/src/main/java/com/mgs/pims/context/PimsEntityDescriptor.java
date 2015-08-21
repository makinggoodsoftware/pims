package com.mgs.pims.context;

import com.mgs.pims.core.linker.PimsLink;
import com.mgs.pims.types.builder.PimsBuilder;
import com.mgs.pims.types.metaData.PimsEntityMetaData;
import com.mgs.pims.types.provider.PimsProvider;
import com.mgs.pims.types.retriever.PimsRetriever;
import com.mgs.reflections.ParsedType;

import java.util.Optional;

public class PimsEntityDescriptor {
    private final ParsedType type;
    private final PimsEntityMetaData metadata;
    private final PimsLink links;
    private final Optional<PimsBuilder> builder;
    private final Optional<PimsProvider> provider;
    private final Optional<PimsRetriever> retriever;

    public PimsEntityDescriptor(ParsedType type, PimsEntityMetaData metadata, PimsLink links, Optional<PimsBuilder> builder, Optional<PimsProvider> provider, Optional<PimsRetriever> retriever) {
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
