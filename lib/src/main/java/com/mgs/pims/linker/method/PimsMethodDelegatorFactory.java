package com.mgs.pims.linker.method;

import com.mgs.pims.annotations.PimsEntity;
import com.mgs.pims.annotations.PimsMethod;
import com.mgs.pims.event.PimsEventType;
import com.mgs.pims.linker.parameters.ParameterResolution;
import com.mgs.pims.linker.parameters.PimsParameters;
import com.mgs.pims.types.base.PimsBaseEntity;
import com.mgs.reflections.Reflections;
import com.mgs.text.PatternMatcher;
import com.mgs.text.PatternMatchingResult;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;

public class PimsMethodDelegatorFactory {
    private final PatternMatcher patternMatcher;
    private final PimsParameters pimsParameters;
    private final Reflections reflections;

    public PimsMethodDelegatorFactory(PatternMatcher patternMatcher, PimsParameters pimsParameters, Reflections reflections) {
        this.patternMatcher = patternMatcher;
        this.pimsParameters = pimsParameters;
        this.reflections = reflections;
    }

    public PimsMethodDelegator link(Method sourceMethod) {
        Class rootEntityType = sourceMethod.getDeclaringClass();
        SortedSet<LinkedMethod> mixerMethods = reflections.walkInterfaceMethods(
                rootEntityType,
                this::managedBy,
                comparator(),
                thisMethod -> {
                    return linkedMethod(sourceMethod, thisMethod);
                }

        );
        if (mixerMethods == null || mixerMethods.size() == 0) {
            throw new IllegalStateException("Can't find the method " + sourceMethod.getName() + " in: " + rootEntityType.getName() + " or its parent classes");
        }
        LinkedMethod mixerMethod = mixerMethods.first();
        List<ParameterResolution> parameterTypes = pimsParameters.parse(mixerMethod);
        Method declaredMethod = mixerMethod.getDeclaredMethod();
        return new PimsMethodDelegator(
                declaredMethod.getDeclaringClass(),
                declaredMethod,
                parameterTypes
        );
    }

    public <T extends PimsBaseEntity> PimsMethodDelegator event(PimsEventType pimsEventType, Class<T> actualType) {
        return null;
    }

    private Optional<LinkedMethod> linkedMethod(Method sourceMethod, Method thisMethod) {
        PimsMethod pimsMethod = thisMethod.getAnnotation(PimsMethod.class);
        if (pimsMethod != null) {
            String pattern = pimsMethod.pattern();
            PatternMatchingResult match = patternMatcher.match(sourceMethod.getName(), pattern);
            if (match.isMatch()) {
                return Optional.of(new LinkedMethod(thisMethod, match.getPlaceholders()));
            }
        }
        return Optional.empty();
    }


    private Comparator<LinkedMethod> comparator() {
        return (left, right) -> {
            int leftFirst = -1;
            int rightFirst = 1;
            int keepCurrentOrder = -1;

            boolean rightHasPlaceholders = hasPlaceholders(right);
            boolean leftHasPlaceholders = hasPlaceholders(left);
            return
                    rightHasPlaceholders == leftHasPlaceholders ? keepCurrentOrder :
                            rightHasPlaceholders ? leftFirst :
                            rightFirst;
        };
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
