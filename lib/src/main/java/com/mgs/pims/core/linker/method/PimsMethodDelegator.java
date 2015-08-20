package com.mgs.pims.core.linker.method;

import com.mgs.pims.core.linker.parameters.ParameterResolution;

import java.lang.reflect.Method;
import java.util.List;

public class PimsMethodDelegator {
	private final Class targetType;
    private final Method delegatorMethod;
    private final List<ParameterResolution> pimsMethodParameterTypes;

	public PimsMethodDelegator(Class targetType, Method delegatorMethod, List<ParameterResolution> pimsMethodParameterTypes) {
		this.targetType = targetType;
        this.delegatorMethod = delegatorMethod;
        this.pimsMethodParameterTypes = pimsMethodParameterTypes;
    }


	public Class getTargetType() {
		return targetType;
	}

    public Method getDelegatorMethod() {
        return delegatorMethod;
    }

    public List<ParameterResolution> getPimsMethodParameterTypes() {
        return pimsMethodParameterTypes;
    }

    @Override
    public String toString() {
        return "PimsMethodDelegator{" +
                ", targetType=" + targetType +
                ", delegatorMethod=" + delegatorMethod +
                ", pimsMethodParameterTypes=" + pimsMethodParameterTypes +
                '}';
    }
}
