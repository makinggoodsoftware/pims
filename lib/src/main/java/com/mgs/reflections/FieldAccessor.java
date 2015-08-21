package com.mgs.reflections;

import java.lang.reflect.Method;
import java.util.List;

public class FieldAccessor {
	private final FieldAccessorType type;
	private final String methodName;
	private final String prefix;
	private final String fieldName;
	private final ParsedType returnType;
	private final List<ParsedType> parameters;
	private final Method method;

	public FieldAccessor(String methodName, String fieldName, String prefix, FieldAccessorType type, ParsedType returnType, List<ParsedType> parameters, Method method) {
		this.methodName = methodName;
		this.type = type;
		this.prefix = prefix;
		this.fieldName = fieldName;
		this.returnType = returnType;
		this.parameters = parameters;
		this.method = method;
	}

	public FieldAccessorType getType() {
		return type;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getMethodName() {
		return methodName;
	}
	public ParsedType getReturnType() {
		return returnType;
	}

	public List<ParsedType> getParameters() {
		return parameters;
	}

	public Method getMethod() {
		return method;
	}

	@SuppressWarnings("RedundantIfStatement")
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof FieldAccessor)) return false;

		FieldAccessor that = (FieldAccessor) o;

		if (!fieldName.equals(that.fieldName)) return false;
		if (!methodName.equals(that.methodName)) return false;
		if (!prefix.equals(that.prefix)) return false;
		if (type != that.type) return false;
		if (!returnType.equals(that.returnType)) return false;
		if (!parameters.equals(that.parameters)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = type.hashCode();
		result = 31 * result + methodName.hashCode();
		result = 31 * result + prefix.hashCode();
		result = 31 * result + fieldName.hashCode();
		result = 31 * result + returnType.hashCode();
		result = 31 * result + parameters.hashCode();
		return result;
	}

	@SuppressWarnings("StringBufferReplaceableByString")
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("FieldAccessor{");
		sb.append("type=").append(type);
		sb.append(", methodName='").append(methodName).append('\'');
		sb.append(", prefix='").append(prefix).append('\'');
		sb.append(", fieldName='").append(fieldName).append('\'');
		sb.append(", returnType=").append(returnType);
		sb.append(", parameters=").append(parameters);
		sb.append('}');
		return sb.toString();
	}

}
