package com.mgs.pims.core.linker.method;

import com.google.common.base.Objects;

import java.lang.reflect.Method;
import java.util.Map;

public class LinkedMethod {
    private final Method declaredMethod;
    private final Map<String, String> placeholders;

    public LinkedMethod(Method declaredMethod, Map<String, String> placeholders) {
        this.declaredMethod = declaredMethod;
        this.placeholders = placeholders;
    }

    public Method getDeclaredMethod() {
        return declaredMethod;
    }

    public Map<String, String> getPlaceholders() {
        return placeholders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LinkedMethod)) return false;
        LinkedMethod that = (LinkedMethod) o;
        return Objects.equal(getDeclaredMethod(), that.getDeclaredMethod()) &&
                Objects.equal(getPlaceholders(), that.getPlaceholders());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getDeclaredMethod(), getPlaceholders());
    }
}
