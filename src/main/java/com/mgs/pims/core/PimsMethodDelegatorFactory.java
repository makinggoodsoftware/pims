package com.mgs.pims.core;

import com.mgs.pims.annotations.PimsEntity;
import com.mgs.pims.annotations.PimsMethod;
import com.mgs.text.PatternMatcher;
import com.mgs.text.PatternMatchingResult;

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
        LinkedMethod mixerMethod = findMixerMethodFrom(sourceMethod, rootEntityType);
        List<ParameterResolution> parameterTypes = pimsParameters.parse (mixerMethod);
        Method declaredMethod = mixerMethod.getDeclaredMethod();
        return new PimsMethodDelegator<>(
                entityType,
                declaredMethod.getDeclaringClass(),
                declaredMethod,
                parameterTypes
        );
    }

    private LinkedMethod findMixerMethodFrom(Method sourceMethod, Class rootEntityType) {
        LinkedMethod mixerMethod;
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

    private LinkedMethod mixerMethod(Class mixerType, Method sourceMethod) {
        for (Method declaredMethod : mixerType.getDeclaredMethods()) {
            PimsMethod pimsMethod = declaredMethod.getAnnotation(PimsMethod.class);
            if (pimsMethod != null){
                String pattern = pimsMethod.pattern();
                PatternMatchingResult match = patternMatcher.match(sourceMethod.getName(), pattern);
                if (match.isMatch()){
                    return new LinkedMethod(declaredMethod, match.getPlaceholders());
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
