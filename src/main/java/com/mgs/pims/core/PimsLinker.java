package com.mgs.pims.core;

import java.lang.reflect.Method;
import java.util.Map;

public class PimsLinker {
    private final PimsMixersProvider pimsMixersProvider;

    public PimsLinker(PimsMixersProvider pimsMixersProvider) {
        this.pimsMixersProvider = pimsMixersProvider;
    }

    public Map<Method, PimsMethodDelegator> link(Class actualType) {
        return null;
    }
}
