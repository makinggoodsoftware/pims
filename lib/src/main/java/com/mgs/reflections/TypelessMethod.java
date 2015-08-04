package com.mgs.reflections;

import com.google.common.base.Objects;

import java.lang.reflect.Method;
import java.util.Arrays;

public class TypelessMethod {
    private final String name;
    private final Class<?> returnType;
    private final Class<?>[] parameterTypes;

    public static TypelessMethod fromMethod (Method method){
        return new TypelessMethod(method.getName(), method.getReturnType(), method.getParameterTypes());
    }

    public TypelessMethod(String name, Class<?> returnType, Class<?>[] parameterTypes) {
        this.name = name;
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
    }

    public String getName() {
        return name;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TypelessMethod)) return false;
        TypelessMethod that = (TypelessMethod) o;
        return Objects.equal(getName(), that.getName()) &&
                Objects.equal(getReturnType(), that.getReturnType()) &&
                Arrays.equals(getParameterTypes(), that.getParameterTypes());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName(), getReturnType(), Arrays.hashCode(getParameterTypes()));
    }

    @Override
    public String toString() {
        return "TypelessMethod{" +
                "name='" + name + '\'' +
                ", returnType=" + returnType +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                '}';
    }
}
