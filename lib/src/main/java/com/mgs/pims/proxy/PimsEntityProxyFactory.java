package com.mgs.pims.proxy;

import com.mgs.pims.linker.PimsLinker;
import com.mgs.pims.linker.method.PimsMethodCaller;
import com.mgs.pims.linker.parameters.PimsParameters;
import com.mgs.pims.types.entity.PimsMapEntity;

import java.util.Map;

import static java.lang.reflect.Proxy.newProxyInstance;

public class PimsEntityProxyFactory {
    private final PimsParameters pimsParameters;
    private final PimsLinker pimsLinker;
    private final PimsMethodCaller pimsMethodCaller;

    public PimsEntityProxyFactory(PimsParameters pimsParameters, PimsLinker pimsLinker, PimsMethodCaller pimsMethodCaller) {
        this.pimsParameters = pimsParameters;
        this.pimsLinker = pimsLinker;
        this.pimsMethodCaller = pimsMethodCaller;
    }

    public <T extends PimsMapEntity> T proxy(boolean mutable, Class actualType, Map<String, Object> valueMap, Map<String, Object> domainMap) {
        //noinspection unchecked
        return (T) newProxyInstance(
                PimsEntityProxyFactory.class.getClassLoader(),
                new Class[]{actualType},
                new PimsEntityProxy(
                        pimsMethodCaller,
                        actualType,
                        valueMap,
                        domainMap,
                        pimsLinker.link(actualType),
                        mutable,
                        pimsParameters)
        );
    }
}
