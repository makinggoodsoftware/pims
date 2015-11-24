package com.mgs.pims.core.linker;

import com.mgs.pims.core.PimsEventType;
import com.mgs.pims.core.linker.method.PimsMethodDelegator;
import com.mgs.reflections.TypelessMethod;

import java.util.Map;

public class PimsLink {
    private final Map<TypelessMethod, PimsMethodDelegator> methods;
    private final Map<PimsEventType, PimsMethodDelegator> events;

    public PimsLink(Map<TypelessMethod, PimsMethodDelegator> methods, Map<PimsEventType, PimsMethodDelegator> events) {
        this.methods = methods;
        this.events = events;
    }

    public Map<TypelessMethod, PimsMethodDelegator> getMethods() {
        return methods;
    }

    public Map<PimsEventType, PimsMethodDelegator> getEvents() {
        return events;
    }
}
