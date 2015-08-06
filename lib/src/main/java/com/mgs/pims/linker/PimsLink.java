package com.mgs.pims.linker;

import com.mgs.pims.event.PimsEventType;
import com.mgs.pims.linker.method.PimsMethodDelegator;
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
