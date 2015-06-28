package com.reflections;

import java.lang.reflect.Method;

public class GenericMethod {
	private final ParsedType returnType;
	private final Method method;

	public GenericMethod(ParsedType returnType, Method method) {
		this.returnType = returnType;
		this.method = method;
	}

	public ParsedType getReturnType() {
		return returnType;
	}

	public Method getMethod() {
		return method;
	}
}
