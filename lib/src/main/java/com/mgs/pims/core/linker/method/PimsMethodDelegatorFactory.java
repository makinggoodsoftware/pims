package com.mgs.pims.core.linker.method;

import com.mgs.pims.annotations.PimsEntity;
import com.mgs.pims.annotations.PimsEvent;
import com.mgs.pims.annotations.PimsMethod;
import com.mgs.pims.core.PimsEventType;
import com.mgs.pims.core.linker.parameters.ParameterResolution;
import com.mgs.pims.core.linker.parameters.PimsParameters;
import com.mgs.pims.types.base.PimsBaseEntity;
import com.mgs.reflections.Reflections;
import com.mgs.text.PatternMatcher;
import com.mgs.text.PatternMatchingResult;

import java.lang.reflect.Method;
import java.util.*;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public class PimsMethodDelegatorFactory {
    private final PatternMatcher patternMatcher;
    private final PimsParameters pimsParameters;
    private final Reflections reflections;

    public PimsMethodDelegatorFactory(
            PatternMatcher patternMatcher,
            PimsParameters pimsParameters,
            Reflections reflections
    ) {
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
        return toDelegator(mixerMethods.first());
    }

    public <T extends PimsBaseEntity> Optional<PimsMethodDelegator> event(PimsEventType pimsEventType, Class<T> from) {
        SortedSet<LinkedMethod> mixerMethods = reflections.walkInterfaceMethods(
                from,
                this::managedBy,
                comparator(),
                thisMethod -> {
                    PimsEvent pimsEvent = thisMethod.getAnnotation(PimsEvent.class);
                    if (pimsEvent != null && pimsEvent.type() == pimsEventType) {
                        return of(new LinkedMethod(thisMethod, new HashMap<>()));
                    }
                    return empty();
                }

        );
        if (mixerMethods == null || mixerMethods.size() == 0) {
            return empty();
        }
        return of(toDelegator(mixerMethods.first()));
    }

    private PimsMethodDelegator toDelegator(LinkedMethod linkedMethod) {
        List<ParameterResolution> parameterTypes = pimsParameters.parse(linkedMethod);
        Method declaredMethod = linkedMethod.getDeclaredMethod();
        return new PimsMethodDelegator(
                declaredMethod.getDeclaringClass(),
                declaredMethod,
                parameterTypes
        );
    }

    private Optional<LinkedMethod> linkedMethod(Method sourceMethod, Method thisMethod) {
        PimsMethod pimsMethod = thisMethod.getAnnotation(PimsMethod.class);
        if (pimsMethod != null) {
            String pattern = pimsMethod.pattern();
            PatternMatchingResult match = patternMatcher.match(sourceMethod.getName(), pattern);
            if (match.isMatch()) {
                return of(new LinkedMethod(thisMethod, match.getPlaceholders()));
            }
        }
        return empty();
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
