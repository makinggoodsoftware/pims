package com.mgs.pims.context;

public class PimsEntityDescriptor {
    private final PimsEntityStaticDescriptor pimsEntityStaticDescriptor;
    private final PimsEntityRelationships pimsEntityRelationships;

    public PimsEntityDescriptor(PimsEntityStaticDescriptor pimsEntityStaticDescriptor, PimsEntityRelationships pimsEntityRelationships) {
        this.pimsEntityStaticDescriptor = pimsEntityStaticDescriptor;
        this.pimsEntityRelationships = pimsEntityRelationships;
    }

    public PimsEntityStaticDescriptor getStaticDescriptor() {
        return pimsEntityStaticDescriptor;
    }

    public PimsEntityRelationships getPimsEntityRelationships() {
        return pimsEntityRelationships;
    }
}
