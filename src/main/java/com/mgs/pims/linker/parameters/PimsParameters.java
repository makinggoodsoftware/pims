package com.mgs.pims.linker.parameters;

import com.mgs.pims.annotations.PimsParameter;
import com.mgs.pims.linker.method.LinkedMethod;

import java.lang.reflect.Parameter;
import java.util.*;

import static com.mgs.pims.linker.parameters.ParameterResolution.simple;
import static com.mgs.pims.linker.parameters.PimsMethodParameterType.METHOD_PARAMETERS;
import static com.mgs.pims.linker.parameters.PimsMethodParameterType.PLACEHOLDER;
import static com.mgs.pims.linker.parameters.PimsMethodParameterType.PROXY_OBJECT;
import static java.util.Collections.singletonList;

public class PimsParameters {
    public Object[] apply(List<ParameterResolution> pimsMethodParameterTypes, Map<PimsMethodParameterType, Object> pimsMethodCallParameters) {
        ArrayList<Object> objects = new ArrayList<>();
        for (ParameterResolution parameterResolution : pimsMethodParameterTypes) {
            PimsMethodParameterType type = parameterResolution.getType();
            if (type == PLACEHOLDER){
                objects.add(parameterResolution.getValue());
            }else{
                Object rawParameterValue = pimsMethodCallParameters.get(type);
                if (type == METHOD_PARAMETERS) {
                    Object [] methodParameters = (Object[]) rawParameterValue;
                    Collections.addAll(objects, methodParameters);
                } else {
                    objects.add(rawParameterValue);
                }
            }
        }
        return objects.toArray();
    }

    public Map<PimsMethodParameterType, Object> from(
            Object proxy,
            Object[] args,
            Map<String, Object> domainMap,
            Map<String, Object> valueMap
    ) {
        if (proxy == null) throw new IllegalStateException("Can't create parameters, proxy ,must be NOT null");
        if (domainMap == null) throw new IllegalStateException("Can't create parameters, domain map ,must be NOT null");
        if (valueMap == null) throw new IllegalStateException("Can't create parameters, value map ,must be NOT null");

        Map<PimsMethodParameterType, Object> parameters = new HashMap<>();
        parameters.put(PimsMethodParameterType.PROXY_OBJECT, proxy);
        parameters.put(METHOD_PARAMETERS, args);
        parameters.put(PimsMethodParameterType.DOMAIN_MAP, domainMap);
        parameters.put(PimsMethodParameterType.VALUE_MAP, valueMap);
        return parameters;
    }

    public List<ParameterResolution> parse(LinkedMethod linkedMethod) {
        Parameter[] parameters = linkedMethod.getDeclaredMethod().getParameters();
        if (parameters.length == 0) throw new IllegalStateException();

        if (parameters.length == 1 && parameters[0].getAnnotations().length == 0){
            return singletonList(simple(PROXY_OBJECT));
        }
        List<ParameterResolution> parameterTypes = new ArrayList<>();
        for (Parameter parameter : parameters) {
            PimsParameter pimsParameter = parameter.getAnnotation(PimsParameter.class);
            if (pimsParameter == null) throw new IllegalStateException();
            PimsMethodParameterType parameterType = pimsParameter.type();
            if (parameterType != PLACEHOLDER) {
                parameterTypes.add(simple(parameterType));
            }else{
                String placeholderName = pimsParameter.name();
                if (placeholderName == null) throw new IllegalStateException();
                parameterTypes.add(ParameterResolution.placeholder(linkedMethod.getPlaceholders().get(placeholderName)));
            }
        }
        return parameterTypes;
    }
}
