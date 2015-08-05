package com.mgs.pims.linker;

import com.mgs.pims.linker.method.PimsMethodDelegator;
import com.mgs.reflections.TypelessMethod;

import java.util.Map;

public class PimsLink {
    private final Map<TypelessMethod, PimsMethodDelegator> methods;

    public PimsLink(Map<TypelessMethod, PimsMethodDelegator> methods) {
        this.methods = methods;
    }

    public Map<TypelessMethod, PimsMethodDelegator> getMethods() {
        return methods;
    }
}
