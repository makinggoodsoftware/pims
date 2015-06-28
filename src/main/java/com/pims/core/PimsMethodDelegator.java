package com.pims.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class PimsMethodDelegator {
	private final Class managerType;
	private final Method delegator;

	public PimsMethodDelegator(Class managerType, Method delegator) {
		this.managerType = managerType;
		this.delegator = delegator;
	}

	public Object delegate(PimsMixersProvider pimsMixersProvider, Class type, Object proxy, Method method, Map<String, Object> domainMap, Map<String, Object> valueMap, boolean open, Object[] args) {
		try {
			return delegator.invoke(pimsMixersProvider.from(managerType), proxy, domainMap, args);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new IllegalStateException(e);
		}
	}
}
