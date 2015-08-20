package com.mgs.pims.context;

import com.mgs.pims.types.base.PimsBaseEntity;
import com.mgs.reflections.ParsedType;

import java.util.Map;

public class PimsContext {
    private final Map<String, PimsEntityDescriptor> descriptors;

    public PimsContext(Map<String, PimsEntityDescriptor> descriptors) {
        this.descriptors = descriptors;
    }

    public PimsEntityDescriptor get(String name) {
        return descriptors.get(name);
    }

    public PimsEntityDescriptor get(Class<? extends PimsBaseEntity> name) {
        return null;
    }

    public PimsEntityDescriptor get(ParsedType type) {
        return null;
    }
}
