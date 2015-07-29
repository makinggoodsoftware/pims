package com.mgs.pims.linker.method;

import com.mgs.pims.annotations.PimsEntity;
import com.mgs.pims.annotations.PimsMethod;
import com.mgs.pims.types.entity.PimsMapEntity;
import com.mgs.pims.linker.parameters.ParameterResolution;
import com.mgs.pims.linker.parameters.PimsParameters;
import com.mgs.text.PatternMatcher;
import com.mgs.text.PatternMatchingResult;

import java.lang.reflect.Method;
import java.util.List;
import java.util.PriorityQueue;

public class PimsMethodDelegatorFactory {
    private final PatternMatcher patternMatcher;
    private final PimsParameters pimsParameters;


    public PimsMethodDelegatorFactory(PatternMatcher patternMatcher, PimsParameters pimsParameters) {
        this.patternMatcher = patternMatcher;
        this.pimsParameters = pimsParameters;
    }

    public <T extends PimsMapEntity> PimsMethodDelegator<T> link(Class<T> entityType, Method sourceMethod) {
        Class rootEntityType = sourceMethod.getDeclaringClass();
        PriorityQueue<LinkedMethod> mixerMethods = findMixerMethodCandidatesFrom(sourceMethod, rootEntityType);
        LinkedMethod mixerMethod = mixerMethods.poll();
        List<ParameterResolution> parameterTypes = pimsParameters.parse (mixerMethod);
        Method declaredMethod = mixerMethod.getDeclaredMethod();
        return new PimsMethodDelegator<>(
                entityType,
                declaredMethod.getDeclaringClass(),
                declaredMethod,
                parameterTypes
        );
    }

    private PriorityQueue<LinkedMethod> findMixerMethodCandidatesFrom(Method sourceMethod, Class rootEntityType) {
        PriorityQueue<LinkedMethod> possibleCandidates = new PriorityQueue<>(
                (left, right) -> {
                    int leftFirst = -1;
                    int rightFirst = 1;
                    int dontCare = 0;

                    return hasPlaceholders(right) ? leftFirst :
                           hasPlaceholders(left) ? rightFirst :
                               dontCare;
                }
        );
        Class thisEntityType = rootEntityType;
        while (thisEntityType != null){
            Class mixerType = managedBy(thisEntityType);
            LinkedMethod mixerMethod = mixerMethod(mixerType, sourceMethod);
            if (mixerMethod != null) possibleCandidates.add(mixerMethod);
            Class[] parentInterfaces = thisEntityType.getInterfaces();
            if (parentInterfaces == null || parentInterfaces.length < 1) {
                thisEntityType = null;
            } else {
                thisEntityType = parentInterfaces[0];
            }
        }
        return possibleCandidates;
    }

    private boolean hasPlaceholders(LinkedMethod linkedMethod) {
        return linkedMethod.getPlaceholders() != null && linkedMethod.getPlaceholders().size() > 0;
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
