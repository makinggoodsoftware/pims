package com.mgs.pims.core;

import com.mgs.pims.annotations.PimsEntity;
import com.mgs.pims.annotations.PimsMethod;
import com.mgs.text.PatternMatcher;

import java.lang.reflect.Method;
import java.util.List;

public class PimsMethodDelegatorFactory {
    private final PimsMixersProvider pimsMixersProvider;
    private final PatternMatcher patternMatcher;
    private final PimsParameters pimsParameters;


    public PimsMethodDelegatorFactory(PimsMixersProvider pimsMixersProvider, PatternMatcher patternMatcher, PimsParameters pimsParameters) {
        this.pimsMixersProvider = pimsMixersProvider;
        this.patternMatcher = patternMatcher;
        this.pimsParameters = pimsParameters;
    }

    public <T extends PimsMapEntity> PimsMethodDelegator<T> link(Class<T> entityType, Method sourceMethod) {
        Class declaredEntityType = sourceMethod.getDeclaringClass();
        PimsEntity annotation = (PimsEntity) declaredEntityType.getAnnotation(PimsEntity.class);
        if (annotation == null) throw new IllegalStateException("Can't link " + declaredEntityType + ". Is not annotated with PimsEntity");
        Class mixerType = annotation.managedBy();
        Object mixer = pimsMixersProvider.from(mixerType);
        Method mixerMethod = mixerMethod(sourceMethod, mixerType);
        if (mixerMethod == null) throw new IllegalStateException("Can't map the method: " + sourceMethod +  " in " + mixerType);
        List<PimsMethodParameterType> parameterTypes = pimsParameters.parse (mixerMethod);
        return new PimsMethodDelegator<>(entityType, mixer, mixerMethod, parameterTypes);
    }

    private Method mixerMethod(Method sourceMethod, Class declaredEntityType) {
        for (Method declaredMethod : declaredEntityType.getDeclaredMethods()) {
            PimsMethod pimsMethod = declaredMethod.getAnnotation(PimsMethod.class);
            if (pimsMethod != null){
                String pattern = pimsMethod.pattern();
                if (patternMatcher.match(sourceMethod.getName(), pattern).isMatch()){
                    return declaredMethod;
                }
            }
        }
        throw new IllegalStateException("Can't link " + sourceMethod + ", in: " + declaredEntityType);
    }

}
