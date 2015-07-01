package com.mgs.pims.core;

import com.mgs.pims.annotations.PimsEntity;
import com.mgs.pims.annotations.PimsMethod;
import com.mgs.pims.annotations.PimsParameter;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import static com.mgs.pims.core.PimsMethodParameterType.DOMAIN_MAP;
import static com.mgs.pims.core.PimsMethodParameterType.SOURCE_OBJECT;

public class PimsMethodDelegatorFactory {
    private final PimsMixersProvider pimsMixersProvider;

    public PimsMethodDelegatorFactory(PimsMixersProvider pimsMixersProvider) {
        this.pimsMixersProvider = pimsMixersProvider;
    }

    public <T extends PimsMapEntity> PimsMethodDelegator<T> link(Class<T> entityType, Method sourceMethod) {
        Class declaredEntityType = sourceMethod.getDeclaringClass();
        PimsEntity annotation = (PimsEntity) declaredEntityType.getAnnotation(PimsEntity.class);
        if (annotation == null) throw new IllegalStateException("Can't link " + declaredEntityType + ". Is not annotated with PimsEntity");
        Class mixerType = annotation.managedBy();
        Object mixer = pimsMixersProvider.from(mixerType);
        Method mixerMethod = mixerMethod(mixerType);
        if (mixerMethod == null) throw new IllegalStateException("Can't map the method: " + sourceMethod +  " in " + mixerType);
        List<PimsMethodParameterType> parameterTypes = new ArrayList<>();
        Parameter[] mixerMethodParameters = mixerMethod.getParameters();
        if (mixerMethodParameters.length == 1){
            PimsParameter pimsParameter = mixerMethodParameters[0].getAnnotation(PimsParameter.class);
            if (pimsParameter == null){
                parameterTypes.add(SOURCE_OBJECT);
            }else{
                parameterTypes.add(pimsParameter.type());
            }
        }
        return new PimsMethodDelegator<>(entityType, mixer, mixerMethod, parameterTypes);
    }

    private Method mixerMethod(Class declaredEntityType) {
        Method mixerMethod = null;
        for (Method declaredMethod : declaredEntityType.getDeclaredMethods()) {
            PimsMethod pimsMethod = declaredMethod.getAnnotation(PimsMethod.class);
            if (pimsMethod != null){
                mixerMethod = declaredMethod;
            }
        }
        return mixerMethod;
    }

}
