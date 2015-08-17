package com.mgs.pims.linker.method;

import com.mgs.pims.linker.mixer.PimsMixersProvider;
import com.mgs.pims.linker.parameters.ParameterResolution;
import com.mgs.pims.linker.parameters.PimsMethodParameterType;
import com.mgs.pims.linker.parameters.PimsParameters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class PimsMethodCaller {
    private final PimsParameters pimsParameters;
    private final PimsMixersProvider pimsMixersProvider;

    public PimsMethodCaller(PimsParameters pimsParameters, PimsMixersProvider pimsMixersProvider) {
        this.pimsParameters = pimsParameters;
        this.pimsMixersProvider = pimsMixersProvider;
    }

    public Object delegate(PimsMethodDelegator pimsMethodDelegator, Map<PimsMethodParameterType, Object> pimsMethodCallParameters) {
        try {
            List<ParameterResolution> pimsMethodParameterTypes = pimsMethodDelegator.getPimsMethodParameterTypes();
            return doDelegate(
                    pimsMethodDelegator,
                    pimsParameters.apply(
                            pimsMethodParameterTypes,
                            pimsMethodCallParameters
                    )
            );
        } catch (Exception e) {
            throw new IllegalStateException (e);
        }
    }

    public Map<String, Object> inputTranslation(PimsMethodDelegator inputTranslator, Map<String, Object> valueMap) {
        try {
            Object[] params = {valueMap};
            //noinspection unchecked
            return (Map<String, Object>) doDelegate(inputTranslator, params);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private Object doDelegate(PimsMethodDelegator pimsMethodDelegator, Object[] params) throws IllegalAccessException, InvocationTargetException {
        Class delegatorType = pimsMethodDelegator.getTargetType();
        Object delegator = pimsMixersProvider.from(delegatorType);
        Method delegatorMethod = pimsMethodDelegator.getDelegatorMethod();
        return delegatorMethod.invoke(delegator, params);
    }
}
