package com.mgs.pims.context;

import java.util.Map;

public class PimsContext {
    private final Map<String, PimsEntityDescriptor> descriptors;

    public PimsContext(Map<String, PimsEntityDescriptor> descriptors) {
        this.descriptors = descriptors;
    }

    PimsEntityDescriptor get (String name){
        return descriptors.get(name);
    }
}
