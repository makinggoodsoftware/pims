package com.mgs.pims.core;

import com.mgs.maps.MapFieldValueFactory;
import com.mgs.maps.MapWalker;
import com.mgs.maps.OnMapFieldCallback;
import com.mgs.reflections.ParsedType;

import java.util.HashMap;
import java.util.Map;

import static java.lang.reflect.Proxy.newProxyInstance;

public class PimsFactory {
    private final PimsLinker pimsLinker;
    private final MapWalker mapWalker;
    private final MapFieldValueFactory mapFieldValueFactory;
    private final PimsMethodCaller pimsMethodCaller;


    public PimsFactory(PimsLinker pimsLinker, MapWalker mapWalker, MapFieldValueFactory mapFieldValueFactory, PimsMethodCaller pimsMethodCaller) {
        this.pimsLinker = pimsLinker;
        this.mapWalker = mapWalker;
        this.mapFieldValueFactory = mapFieldValueFactory;
        this.pimsMethodCaller = pimsMethodCaller;
    }

    public <T extends PimsMapEntity> T immutable (Class<T> type, Map<String, Object> valueMap){
        return null;
    }

    public <T extends PimsMapEntity> T immutable(ParsedType type, Map<String, Object> valueMap) {
        return fromValueMap(type, valueMap, false);
    }

    public PimsMapEntity mutable(PimsMapEntity pimsMapEntity) {
        return ofMutability(pimsMapEntity, true);
    }

    public PimsMapEntity immutable(PimsMapEntity pimsMapEntity) {
        return ofMutability(pimsMapEntity, false);
    }

    private PimsMapEntity ofMutability(PimsMapEntity pimsMapEntity, boolean mutable) {
        if (pimsMapEntity.isMutable() == mutable) return pimsMapEntity;
        return proxy(mutable, pimsMapEntity.getType(), pimsMapEntity.getValueMap(), pimsMapEntity.getDomainMap());
    }

    private <T extends PimsMapEntity> T fromValueMap(ParsedType type, Map<String, Object> valueMap, boolean mutable) {
        Class actualType = type.getActualType().get();
        OnMapFieldCallback onSpecialCaseProcessor = (fieldAccessor, value) -> {
            //noinspection unchecked
            Map<String, Object> castedValue = (Map<String, Object>) value;
            return fromValueMap(fieldAccessor.getReturnType(), castedValue, mutable);
        };
        Map<String, Object> transformed = new HashMap<>();
        mapWalker.walk(valueMap, type, new PimsMapEntityFieldCallback(onSpecialCaseProcessor, transformed, mapFieldValueFactory));
        Map<String, Object> domainMap = transformed;
        return proxy(mutable, actualType, valueMap, domainMap);

    }

    private <T extends PimsMapEntity> T proxy(boolean mutable, Class actualType, Map<String, Object> valueMap, Map<String, Object> domainMap) {
        //noinspection unchecked
        return (T) newProxyInstance(
                PimsFactory.class.getClassLoader(),
                new Class[]{actualType},
                new PimsEntityProxy(
                        pimsMethodCaller,
                        actualType,
                        valueMap,
                        domainMap,
                        pimsLinker.link(actualType),
                        mutable
                )
        );
    }
}
