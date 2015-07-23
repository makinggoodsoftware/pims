package com.mgs.pims.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

public class PimsEntityProxy implements InvocationHandler {
	private final Class<? extends PimsMapEntity> type;
	private final Map<String, Object> domainMap;
	private final Map<String, Object> valueMap;
	private final Map<Method, PimsMethodDelegator> methodDelegators;
	private final boolean mutable;
	private final PimsMethodCaller pimsMethodCaller;

	public PimsEntityProxy(PimsMethodCaller pimsMethodCaller, Class type, Map<String, Object> domainMap, Map<String, Object> valueMap, Map<Method, PimsMethodDelegator> methodDelegators, boolean mutable) {
		this.type = type;
		this.domainMap = domainMap;
		this.valueMap = valueMap;
		this.methodDelegators = methodDelegators;
		this.mutable = mutable;
        this.pimsMethodCaller = pimsMethodCaller;
    }

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return pimsMethodCaller.delegate(methodDelegators.get(method), new PimsMethodCallParameters(proxy, args));
	}

	public Map<String, Object> getDomainMap() {
		return domainMap;
	}

	public Map<Method, PimsMethodDelegator> getMethodDelegators() {
		return methodDelegators;
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
