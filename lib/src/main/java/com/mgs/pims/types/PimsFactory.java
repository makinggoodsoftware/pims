package com.mgs.pims.types;

import com.mgs.maps.MapTransformer;
import com.mgs.pims.linker.PimsLink;
import com.mgs.pims.linker.PimsLinker;
import com.mgs.pims.linker.method.PimsMethodCaller;
import com.mgs.pims.linker.method.PimsMethodDelegator;
import com.mgs.pims.linker.parameters.PimsParameters;
import com.mgs.pims.proxy.PimsEntityProxy;
import com.mgs.pims.types.base.PimsBaseEntity;
import com.mgs.reflections.ParsedType;

import java.util.Map;

import static com.mgs.pims.event.PimsEventType.INPUT_TRANSLATION;
import static java.lang.reflect.Proxy.newProxyInstance;

public class PimsFactory {
    private final MapTransformer mapTransformer;
    private final PimsParameters pimsParameters;
    private final PimsLinker pimsLinker;
    private final PimsMethodCaller pimsMethodCaller;

    public PimsFactory(MapTransformer mapTransformer, PimsParameters pimsParameters, PimsLinker pimsLinker, PimsMethodCaller pimsMethodCaller) {
        this.mapTransformer = mapTransformer;
        this.pimsParameters = pimsParameters;
        this.pimsLinker = pimsLinker;
        this.pimsMethodCaller = pimsMethodCaller;
    }

    public <T extends PimsBaseEntity> T immutable(ParsedType type, Map<String, Object> valueMap) {
        return fromValueMap(type, valueMap, false);
    }

    public <T extends PimsBaseEntity> T mutable(ParsedType type, Map<String, Object> valueMap, Map<String, Object> domainMap) {
        return withMutability(type, valueMap, domainMap, true, pimsLinker.link(type.getActualType().get()));
    }

    public <T extends PimsBaseEntity> T immutable(ParsedType type, Map<String, Object> valueMap, Map<String, Object> domainMap) {
        return withMutability(type, valueMap, domainMap, false, pimsLinker.link(type.getActualType().get()));
    }

    private <T extends PimsBaseEntity> T fromValueMap(ParsedType type, Map<String, Object> valueMap, boolean mutable) {
        Map<String, Object> src = valueMap;
        @SuppressWarnings("unchecked") Class<T> actualClass = type.getOwnDeclaration().getActualType().get();
        PimsLink link = pimsLinker.link(actualClass);
        //noinspection unchecked
        PimsMethodDelegator inputTranslator = link.getEvents().get(INPUT_TRANSLATION);
        if (inputTranslator != null) {
            src = pimsMethodCaller.inputTranslation(inputTranslator, valueMap);
        }
        Map<String, Object> domainMap = mapTransformer.objectify(
                type,
                src,
                (childType, childMap) -> fromValueMap(childType, childMap, mutable));
        //noinspection unchecked
        return (T) withMutability(type, valueMap, domainMap, mutable, link);
    }

    private <T extends PimsBaseEntity> T withMutability(ParsedType type, Map<String, Object> valueMap, Map<String, Object> domainMap, boolean mutable, PimsLink link) {
        return proxy(
                mutable,
                type,
                valueMap,
                domainMap,
                link
        );
    }

    private <T extends PimsBaseEntity> T proxy(boolean mutable, ParsedType type, Map<String, Object> valueMap, Map<String, Object> domainMap, PimsLink link) {


        //noinspection unchecked
        return (T) newProxyInstance(
                PimsFactory.class.getClassLoader(),
                new Class[]{type.getActualType().get()},
                new PimsEntityProxy(
                        pimsMethodCaller,
                        type,
                        domainMap,
                        valueMap,
                        link.getMethods(),
                        mutable,
                        pimsParameters)
        );
    }
}
