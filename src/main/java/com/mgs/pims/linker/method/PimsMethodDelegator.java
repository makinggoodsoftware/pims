package com.mgs.pims.linker.method;

import com.mgs.pims.types.entity.PimsMapEntity;
import com.mgs.pims.linker.parameters.ParameterResolution;

import java.lang.reflect.Method;
import java.util.List;

public class PimsMethodDelegator <T extends PimsMapEntity>{
	private final Class<T> pimsEntityType;
	private final Class targetType;
    private final Method delegatorMethod;
    private final List<ParameterResolution> pimsMethodParameterTypes;

	public PimsMethodDelegator(Class<T> pimsEntityType, Class targetType, Method delegatorMethod, List<ParameterResolution> pimsMethodParameterTypes) {
		this.pimsEntityType = pimsEntityType;
		this.targetType = targetType;
        this.delegatorMethod = delegatorMethod;
        this.pimsMethodParameterTypes = pimsMethodParameterTypes;
    }

	public Class<T> getPimsEntityType() {
		return pimsEntityType;
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
}
