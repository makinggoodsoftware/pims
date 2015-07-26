package com.mgs.pims.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PimsParameters {
    public Object[] parse(List<PimsMethodParameterType> pimsMethodParameterTypes, Map<PimsMethodParameterType, Object> pimsMethodCallParameters) {
        ArrayList<Object> objects = new ArrayList<>();
        for (PimsMethodParameterType pimsMethodParameterType : pimsMethodParameterTypes) {
            Object rawParameterValue = pimsMethodCallParameters.get(pimsMethodParameterType);
            if (pimsMethodParameterType == PimsMethodParameterType.METHOD_PARAMETERS) {
                Object [] methodParameters = (Object[]) rawParameterValue;
                for (Object methodParameter : methodParameters) {
                    objects.add(methodParameter);
                }
            } else {
                objects.add(rawParameterValue);
            }
        }
        return objects.toArray();
    }

    public Map<PimsMethodParameterType, Object> from(Object proxy, Object[] args) {
        Map<PimsMethodParameterType, Object> parameters = new HashMap<>();
        parameters.put(PimsMethodParameterType.SOURCE_OBJECT, proxy);
        parameters.put(PimsMethodParameterType.METHOD_PARAMETERS, args);
        return parameters;
    }
}
