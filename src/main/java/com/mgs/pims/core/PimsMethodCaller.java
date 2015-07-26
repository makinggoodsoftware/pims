package com.mgs.pims.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class PimsMethodCaller {
    private final PimsParameters pimsParameters;

    public PimsMethodCaller(PimsParameters pimsParameters) {
        this.pimsParameters = pimsParameters;
    }

    public Object delegate(PimsMethodDelegator pimsMethodDelegator, Map<PimsMethodParameterType, Object> pimsMethodCallParameters) {
        try {
            Object delegator = pimsMethodDelegator.getDelegator();
            Method delegatorMethod = pimsMethodDelegator.getDelegatorMethod();
            List<PimsMethodParameterType> pimsMethodParameterTypes = pimsMethodDelegator.getPimsMethodParameterTypes();
            Object[] params = pimsParameters.parse(pimsMethodParameterTypes, pimsMethodCallParameters);
            return delegatorMethod.invoke(delegator, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException (e);
        }
    }
}
