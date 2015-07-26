package com.mgs.pims.core;

import com.mgs.pims.annotations.PimsParameter;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

import static com.mgs.pims.core.PimsMethodParameterType.PROXY_OBJECT;
import static java.util.Collections.singletonList;

public class PimsParameters {
    public Object[] apply(List<PimsMethodParameterType> pimsMethodParameterTypes, Map<PimsMethodParameterType, Object> pimsMethodCallParameters) {
        ArrayList<Object> objects = new ArrayList<>();
        for (PimsMethodParameterType pimsMethodParameterType : pimsMethodParameterTypes) {
            Object rawParameterValue = pimsMethodCallParameters.get(pimsMethodParameterType);
            if (pimsMethodParameterType == PimsMethodParameterType.METHOD_PARAMETERS) {
                Object [] methodParameters = (Object[]) rawParameterValue;
                Collections.addAll(objects, methodParameters);
            } else {
                objects.add(rawParameterValue);
            }
        }
        return objects.toArray();
    }

    public Map<PimsMethodParameterType, Object> from(Object proxy, Object[] args, Map<String, Object> domainMap, Map<String, Object> valueMap) {
        Map<PimsMethodParameterType, Object> parameters = new HashMap<>();
        parameters.put(PimsMethodParameterType.PROXY_OBJECT, proxy);
        parameters.put(PimsMethodParameterType.METHOD_PARAMETERS, args);
        parameters.put(PimsMethodParameterType.DOMAIN_MAP, domainMap);
        parameters.put(PimsMethodParameterType.VALUE_MAP, valueMap);
        return parameters;
    }

    public List<PimsMethodParameterType> parse(Method mixerMethod) {
        Parameter[] parameters = mixerMethod.getParameters();
        if (parameters.length == 0) throw new IllegalStateException();

        if (parameters.length == 1 && parameters[0].getAnnotations().length == 0){
            return singletonList(PROXY_OBJECT);
        }
        List<PimsMethodParameterType> parameterTypes = new ArrayList<>();
        for (Parameter parameter : parameters) {
            PimsParameter pimsParameter = parameter.getAnnotation(PimsParameter.class);
            if (pimsParameter == null) throw new IllegalStateException();
            parameterTypes.add(pimsParameter.type());
        }
        return parameterTypes;
    }
}
