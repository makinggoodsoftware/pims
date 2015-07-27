package com.mgs.pims.core;

import com.mgs.pims.annotations.PimsEntity;
import com.mgs.pims.annotations.PimsMethod;
import com.mgs.text.PatternMatcher;

import java.lang.reflect.Method;
import java.util.List;

public class PimsMethodDelegatorFactory {
    private final PatternMatcher patternMatcher;
    private final PimsParameters pimsParameters;


    public PimsMethodDelegatorFactory(PatternMatcher patternMatcher, PimsParameters pimsParameters) {
        this.patternMatcher = patternMatcher;
        this.pimsParameters = pimsParameters;
    }

    public <T extends PimsMapEntity> PimsMethodDelegator<T> link(Class<T> entityType, Method sourceMethod) {
        Class rootEntityType = sourceMethod.getDeclaringClass();
        Method mixerMethod = findMixerMethodFrom(sourceMethod, rootEntityType);
        List<PimsMethodParameterType> parameterTypes = pimsParameters.parse (mixerMethod);
        return new PimsMethodDelegator<>(entityType, mixerMethod.getDeclaringClass(), mixerMethod, parameterTypes);
    }

    private Method findMixerMethodFrom(Method sourceMethod, Class rootEntityType) {
        Method mixerMethod;
        Class thisEntityType = rootEntityType;
        while (true){
            Class mixerType = managedBy(thisEntityType);
            mixerMethod = mixerMethod(mixerType, sourceMethod);
            if (mixerMethod != null) return mixerMethod;
            Class[] parentInterfaces = thisEntityType.getInterfaces();
            if (parentInterfaces == null || parentInterfaces.length < 1) throw new IllegalStateException("Can't link the method!");
            thisEntityType = parentInterfaces[0];
        }
    }

    private Method mixerMethod(Class mixerType, Method sourceMethod) {
        for (Method declaredMethod : mixerType.getDeclaredMethods()) {
            PimsMethod pimsMethod = declaredMethod.getAnnotation(PimsMethod.class);
            if (pimsMethod != null){
                String pattern = pimsMethod.pattern();
                if (patternMatcher.match(sourceMethod.getName(), pattern).isMatch()){
                    return declaredMethod;
                }
            }
        }
        return null;
    }

    private Class managedBy(Class declaredEntityType) {
        PimsEntity annotation = (PimsEntity) declaredEntityType.getAnnotation(PimsEntity.class);
        if (annotation == null) throw new IllegalStateException("Can't link " + declaredEntityType + ". Is not annotated with PimsEntity");
        return annotation.managedBy();
    }

}
