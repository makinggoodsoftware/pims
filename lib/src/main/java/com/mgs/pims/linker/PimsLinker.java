package com.mgs.pims.linker;

import com.mgs.pims.linker.method.PimsMethodDelegator;
import com.mgs.pims.linker.method.PimsMethodDelegatorFactory;
import com.mgs.pims.types.entity.PimsMapEntity;
import com.mgs.reflections.TypelessMethod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class PimsLinker {
    private final PimsMethodDelegatorFactory pimsMethodDelegatorFactory;


    public PimsLinker(PimsMethodDelegatorFactory pimsMethodDelegatorFactory) {
        this.pimsMethodDelegatorFactory = pimsMethodDelegatorFactory;
    }

    public <T extends PimsMapEntity> Map<TypelessMethod, PimsMethodDelegator<T>> link(Class<T> actualType) {
        Map<TypelessMethod, PimsMethodDelegator<T>> linkedMethods = new HashMap<>();
        Method[] methods = actualType.getMethods();
        for (Method method : methods) {
            linkedMethods.put(TypelessMethod.fromMethod(method), pimsMethodDelegatorFactory.link(actualType, method));
        }
        return linkedMethods;
    }


}
