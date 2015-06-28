package com.pims.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

public class PimsEntityProxy implements InvocationHandler {
	private final Class<? extends PimsMapEntity> type;
	private final Map<String, Object> domainMap;
	private final Map<String, Object> valueMap;
	private final Map<Method, PimsMethodDelegator> methodManagers;
	private final boolean mutable;

	public PimsEntityProxy(Class type, Map<String, Object> domainMap, Map<String, Object> valueMap, Map<Method, PimsMethodDelegator> methodManagers, boolean mutable) {
		this.type = type;
		this.domainMap = domainMap;
		this.valueMap = valueMap;
		this.methodManagers = methodManagers;
		this.mutable = mutable;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return methodManagers.get(method).delegate(null, type, proxy, method, domainMap, valueMap, mutable, args);
	}

	public Map<String, Object> getDomainMap() {
		return domainMap;
	}

	public Map<Method, PimsMethodDelegator> getMethodManagers() {
		return methodManagers;
	}

	public boolean isMutable() {
		return mutable;
	}

	public Class<? extends PimsMapEntity> getType() {
		return type;
	}

	public Map<String, Object> getValueMap() {
		return valueMap;
	}
}
