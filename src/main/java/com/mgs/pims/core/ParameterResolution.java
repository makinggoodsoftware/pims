package com.mgs.pims.core;

import com.google.common.base.Objects;

public class ParameterResolution {
    private final PimsMethodParameterType simpleType;
    private final String value;

    public static ParameterResolution simple(PimsMethodParameterType simpleType) {
        return new ParameterResolution(simpleType, null);
    }

    public static ParameterResolution placeholder(String value) {
        return new ParameterResolution(PimsMethodParameterType.PLACEHOLDER, value);
    }

    public ParameterResolution(PimsMethodParameterType simpleType, String value) {
        this.simpleType = simpleType;
        this.value = value;
    }

    public PimsMethodParameterType getType() {
        return simpleType;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParameterResolution)) return false;
        ParameterResolution that = (ParameterResolution) o;
        return simpleType == that.simpleType &&
                Objects.equal(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(simpleType, getValue());
    }
}
