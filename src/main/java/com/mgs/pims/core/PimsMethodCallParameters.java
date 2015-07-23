package com.mgs.pims.core;

import java.util.HashMap;
import java.util.Map;

public class PimsMethodCallParameters {
    private final Map<PimsMethodParameterType, Object> parameters = new HashMap<>();

    public PimsMethodCallParameters(Object proxy, Object[] args) {
        parameters.put(PimsMethodParameterType.SOURCE_OBJECT, proxy);
        parameters.put(PimsMethodParameterType.METHOD_PARAMETERS, args);
    }

    public Object get(PimsMethodParameterType pimsMethodParameterType) {
        return parameters.get(pimsMethodParameterType);
    }
}
