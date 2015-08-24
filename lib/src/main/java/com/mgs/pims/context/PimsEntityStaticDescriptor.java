package com.mgs.pims.context;

import com.mgs.pims.core.linker.PimsLink;
import com.mgs.pims.types.metaData.PimsEntityMetaData;
import com.mgs.reflections.ParsedType;

public class PimsEntityStaticDescriptor {
    private final ParsedType type;
    private final PimsEntityMetaData metadata;
    private final PimsLink links;

    public PimsEntityStaticDescriptor(ParsedType type, PimsEntityMetaData metadata, PimsLink links) {
        this.type = type;
        this.metadata = metadata;
        this.links = links;
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

}
