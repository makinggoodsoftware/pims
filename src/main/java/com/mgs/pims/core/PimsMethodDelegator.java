package com.mgs.pims.core;

import java.lang.reflect.Method;
import java.util.List;

public class PimsMethodDelegator <T extends PimsMapEntity>{
	private final Class<T> sourceType;
	private final Object delegator;
    private final Method delegatorMethod;
    private final List<PimsMethodParameterType> pimsMethodParameterTypes;

	public PimsMethodDelegator(Class<T> sourceType, Object delegator, Method delegatorMethod, List<PimsMethodParameterType> pimsMethodParameterTypes) {
		this.sourceType = sourceType;
		this.delegator = delegator;
        this.delegatorMethod = delegatorMethod;
        this.pimsMethodParameterTypes = pimsMethodParameterTypes;
    }

	public Class<T> getSourceType() {
		return sourceType;
	}

	public Object getDelegator() {
		return delegator;
	}

    public Method getDelegatorMethod() {
        return delegatorMethod;
    }

    public List<PimsMethodParameterType> getPimsMethodParameterTypes() {
        return pimsMethodParameterTypes;
    }
}
