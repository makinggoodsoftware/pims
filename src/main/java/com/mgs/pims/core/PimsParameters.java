package com.mgs.pims.core;

import java.util.ArrayList;
import java.util.List;

public class PimsParameters {
    public Object[] parse(List<PimsMethodParameterType> pimsMethodParameterTypes, PimsMethodCallParameters pimsMethodCallParameters) {
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
}
