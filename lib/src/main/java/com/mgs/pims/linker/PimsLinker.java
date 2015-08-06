package com.mgs.pims.linker;

import com.mgs.pims.event.PimsEventType;
import com.mgs.pims.linker.method.PimsMethodDelegator;
import com.mgs.pims.linker.method.PimsMethodDelegatorFactory;
import com.mgs.pims.types.base.PimsBaseEntity;
import com.mgs.reflections.TypelessMethod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class PimsLinker {
    private final PimsMethodDelegatorFactory pimsMethodDelegatorFactory;


    public PimsLinker(PimsMethodDelegatorFactory pimsMethodDelegatorFactory) {
        this.pimsMethodDelegatorFactory = pimsMethodDelegatorFactory;
    }

    public <T extends PimsBaseEntity> PimsLink link(Class<T> actualType) {
        Map<TypelessMethod, PimsMethodDelegator> linkedMethods = new HashMap<>();
        Method[] methods = actualType.getMethods();
        for (Method method : methods) {
            linkedMethods.put(TypelessMethod.fromMethod(method), pimsMethodDelegatorFactory.link(method));
        }
        Map<PimsEventType, PimsMethodDelegator> events = new HashMap<>();
        for (PimsEventType pimsEventType : PimsEventType.values()) {
            pimsMethodDelegatorFactory.event(pimsEventType, actualType);
        }
        return new PimsLink(linkedMethods, events);
    }


}
