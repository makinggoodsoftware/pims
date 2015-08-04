package com.mgs.pims.proxy;

import com.mgs.pims.linker.method.PimsMethodCaller;
import com.mgs.pims.linker.method.PimsMethodDelegator;
import com.mgs.pims.linker.parameters.PimsParameters;
import com.mgs.pims.types.entity.PimsMapEntity;
import com.mgs.reflections.ParsedType;
import com.mgs.reflections.TypelessMethod;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

public class PimsEntityProxy implements InvocationHandler {
    private final ParsedType type;
    private final Map<String, Object> domainMap;
    private final Map<String, Object> valueMap;
    private final Map<TypelessMethod, PimsMethodDelegator> methodDelegators;
    private final boolean mutable;
    private final PimsMethodCaller pimsMethodCaller;
    private final PimsParameters pimsParameters;

    public PimsEntityProxy(PimsMethodCaller pimsMethodCaller, ParsedType type, Map<String, Object> domainMap, Map<String, Object> valueMap, Map<TypelessMethod, PimsMethodDelegator> methodDelegators, boolean mutable, PimsParameters pimsParameters) {
        this.type = type;
        this.domainMap = domainMap;
        this.valueMap = valueMap;
        this.methodDelegators = methodDelegators;
        this.mutable = mutable;
        this.pimsMethodCaller = pimsMethodCaller;
        this.pimsParameters = pimsParameters;
    }

    @Override
    public Object invoke(Object entity, Method method, Object[] args) throws Throwable {
        TypelessMethod key = TypelessMethod.fromMethod(method);
        PimsMethodDelegator pimsMethodDelegator = methodDelegators.get(key);
        if (pimsMethodDelegator == null){
            String errMsg1 = "Can't find delegator for: " + method.getName() + "[" + method.getDeclaringClass().getName() + "]";
            String errMsg2 = "The available delegators are: " + methodDelegators;
            throw new IllegalStateException(errMsg1 + ". " + errMsg2);
        }
        return pimsMethodCaller.delegate(
                pimsMethodDelegator,
                pimsParameters.from(
                        type,
                        (PimsMapEntity) entity,
                        this,
                        args,
                        domainMap,
                        valueMap
                )
        );
    }

    public Map<String, Object> getDomainMap() {
        return domainMap;
    }

    public Map<TypelessMethod, PimsMethodDelegator> getMethodDelegators() {
        return methodDelegators;
    }

    public boolean isMutable() {
        return mutable;
    }

    public ParsedType getType() {
        return type;
    }

    public Map<String, Object> getValueMap() {
        return valueMap;
    }
}
