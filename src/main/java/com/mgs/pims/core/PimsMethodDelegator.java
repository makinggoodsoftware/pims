package com.mgs.pims.core;

import java.lang.reflect.Method;
import java.util.List;

public class PimsMethodDelegator <T extends PimsMapEntity>{
	private final Class<T> pimsEntityType;
	private final Class targetType;
    private final Method delegatorMethod;
    private final List<PimsMethodParameterType> pimsMethodParameterTypes;

	public PimsMethodDelegator(Class<T> pimsEntityType, Class targetType, Method delegatorMethod, List<PimsMethodParameterType> pimsMethodParameterTypes) {
		this.pimsEntityType = pimsEntityType;
		this.targetType = targetType;
        this.delegatorMethod = delegatorMethod;
        this.pimsMethodParameterTypes = pimsMethodParameterTypes;
    }

	public Class<T> getPimsEntityType() {
		return pimsEntityType;
	}

	public Object getTargetType() {
		return targetType;
	}

    public Method getDelegatorMethod() {
        return delegatorMethod;
    }

    public List<PimsMethodParameterType> getPimsMethodParameterTypes() {
        return pimsMethodParameterTypes;
    }
}
