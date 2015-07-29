package com.mgs.pims.core.linker;

import com.mgs.pims.core.entity.PimsMapEntity;
import com.mgs.pims.core.linker.method.PimsMethodDelegator;
import com.mgs.pims.core.linker.method.PimsMethodDelegatorFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class PimsLinker {
    private final PimsMethodDelegatorFactory pimsMethodDelegatorFactory;


    public PimsLinker(PimsMethodDelegatorFactory pimsMethodDelegatorFactory) {
        this.pimsMethodDelegatorFactory = pimsMethodDelegatorFactory;
    }

    public <T extends PimsMapEntity> Map<Method, PimsMethodDelegator<T>> link(Class<T> actualType) {
        Map<Method, PimsMethodDelegator<T>> linkedMethods = new HashMap<>();
        Method[] methods = actualType.getMethods();
        for (Method method : methods) {
            linkedMethods.put(method, pimsMethodDelegatorFactory.link(actualType, method));
        }
        return linkedMethods;
    }


}
