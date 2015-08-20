package com.mgs.pims.core.linker.parameters;

import com.mgs.pims.annotations.PimsParameter;
import com.mgs.pims.core.linker.method.LinkedMethod;
import com.mgs.pims.proxy.PimsEntityProxy;
import com.mgs.pims.types.base.PimsBaseEntity;
import com.mgs.reflections.FieldAccessor;
import com.mgs.reflections.ParsedType;

import java.lang.reflect.Parameter;
import java.util.*;

import static com.mgs.pims.core.linker.parameters.ParameterResolution.simple;
import static com.mgs.pims.core.linker.parameters.PimsMethodParameterType.*;
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
            ParsedType type,
            PimsBaseEntity entity,
            PimsEntityProxy proxy,
            Object[] args,
            Map<String, FieldAccessor> fieldAccessors,
            Map<String, Object> domainMap,
            Map<String, Object> valueMap
    ) {
        if (type == null) throw new IllegalStateException("Can't create parameters, type ,must be NOT null");
        if (entity == null) throw new IllegalStateException("Can't create parameters, entity ,must be NOT null");
        if (proxy == null) throw new IllegalStateException("Can't create parameters, proxy ,must be NOT null");
        if (fieldAccessors == null)
            throw new IllegalStateException("Can't create parameters, field accessors ,must be NOT null");
        if (domainMap == null) throw new IllegalStateException("Can't create parameters, domain map ,must be NOT null");
        if (valueMap == null) throw new IllegalStateException("Can't create parameters, value map ,must be NOT null");

        Map<PimsMethodParameterType, Object> parameters = new HashMap<>();
        parameters.put(SOURCE_TYPE, type);
        parameters.put(PROXY_OBJECT, proxy);
        parameters.put(SOURCE_OBJECT, entity);
        parameters.put(METHOD_PARAMETERS, args);
        parameters.put(DOMAIN_MAP, domainMap);
        parameters.put(VALUE_MAP, valueMap);
        parameters.put(FIELD_ACCESSORS, fieldAccessors);
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

            PimsMethodParameterType parameterType = pimsParameter.type();
            if (parameterType != PLACEHOLDER) {
                parameterTypes.add(simple(parameterType));
            } else {
                String placeholderName = pimsParameter.name();
                if (placeholderName == null) throw new IllegalStateException();
                parameterTypes.add(ParameterResolution.placeholder(linkedMethod.getPlaceholders().get(placeholderName)));
            }

            if (parameterType == METHOD_PARAMETERS) {
                return parameterTypes;
            }
        }
        return parameterTypes;
    }
}
