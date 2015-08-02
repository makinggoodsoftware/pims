package com.mgs.pims.linker.method;

import com.mgs.pims.annotations.PimsEntity;
import com.mgs.pims.annotations.PimsMethod;
import com.mgs.pims.linker.parameters.ParameterResolution;
import com.mgs.pims.linker.parameters.PimsParameters;
import com.mgs.pims.types.entity.PimsMapEntity;
import com.mgs.text.PatternMatcher;
import com.mgs.text.PatternMatchingResult;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class PimsMethodDelegatorFactory {
    private final PatternMatcher patternMatcher;
    private final PimsParameters pimsParameters;


    public PimsMethodDelegatorFactory(PatternMatcher patternMatcher, PimsParameters pimsParameters) {
        this.patternMatcher = patternMatcher;
        this.pimsParameters = pimsParameters;
    }

    public <T extends PimsMapEntity> PimsMethodDelegator<T> link(Class<T> entityType, Method sourceMethod) {
        Class rootEntityType = sourceMethod.getDeclaringClass();
        SortedSet<LinkedMethod> mixerMethods = findMixerMethodCandidatesFrom(sourceMethod, rootEntityType);
        LinkedMethod mixerMethod = mixerMethods.first();
        List<ParameterResolution> parameterTypes = pimsParameters.parse(mixerMethod);
        Method declaredMethod = mixerMethod.getDeclaredMethod();
        return new PimsMethodDelegator<>(
                entityType,
                declaredMethod.getDeclaringClass(),
                declaredMethod,
                parameterTypes
        );
    }

    private SortedSet<LinkedMethod> findMixerMethodCandidatesFrom(Method sourceMethod, Class rootEntityType) {
        SortedSet<LinkedMethod> possibleCandidates = newLinkedMethodsSetWithPlaceholdersLast();

        Class mixerType = managedBy(rootEntityType);
        SortedSet<LinkedMethod> mixerMethods = mixerMethods(mixerType, sourceMethod);
        possibleCandidates.addAll(mixerMethods);
        Class[] parentInterfaces = rootEntityType.getInterfaces();
        for (Class parentInterface : parentInterfaces) {
            possibleCandidates.addAll(findMixerMethodCandidatesFrom(sourceMethod, parentInterface));
        }
        return possibleCandidates;
    }

    private SortedSet<LinkedMethod> mixerMethods(Class mixerType, Method sourceMethod) {
        SortedSet<LinkedMethod> possibleCandidates = newLinkedMethodsSetWithPlaceholdersLast();

        for (Method declaredMethod : mixerType.getDeclaredMethods()) {
            PimsMethod pimsMethod = declaredMethod.getAnnotation(PimsMethod.class);
            if (pimsMethod != null) {
                String pattern = pimsMethod.pattern();
                PatternMatchingResult match = patternMatcher.match(sourceMethod.getName(), pattern);
                if (match.isMatch()) {
                    possibleCandidates.add(new LinkedMethod(declaredMethod, match.getPlaceholders()));
                }
            }
        }
        return possibleCandidates;
    }

    private TreeSet<LinkedMethod> newLinkedMethodsSetWithPlaceholdersLast() {
        return new TreeSet<>((Comparator<LinkedMethod>) (left, right) -> {
            int leftFirst = -1;
            int rightFirst = 1;
            int keepCurrentOrder = -1;

            boolean rightHasPlaceholders = hasPlaceholders(right);
            boolean leftHasPlaceholders = hasPlaceholders(left);
            return
                    rightHasPlaceholders == leftHasPlaceholders ? keepCurrentOrder :
                            rightHasPlaceholders ? leftFirst :
                            rightFirst;
        });
    }

    private boolean hasPlaceholders(LinkedMethod linkedMethod) {
        return linkedMethod.getPlaceholders() != null && linkedMethod.getPlaceholders().size() > 0;
    }

    private Class managedBy(Class declaredEntityType) {
        PimsEntity annotation = (PimsEntity) declaredEntityType.getAnnotation(PimsEntity.class);
        if (annotation == null)
            throw new IllegalStateException("Can't link " + declaredEntityType + ". Is not annotated with PimsEntity");
        return annotation.managedBy();
    }

}
