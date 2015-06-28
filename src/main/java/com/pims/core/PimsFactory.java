package com.pims.core;

import com.reflections.ParsedType;

import java.util.Map;

import static java.lang.reflect.Proxy.newProxyInstance;

public class PimsFactory {
    private final MapEntityTransformer mapEntityTransformer;
    private final PimsLinker pimsLinker;

    public PimsFactory(MapEntityTransformer mapEntityTransformer, PimsLinker pimsLinker) {
        this.mapEntityTransformer = mapEntityTransformer;
        this.pimsLinker = pimsLinker;
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
        return proxy(mutable, pimsMapEntity.getType().getActualType().get(), pimsMapEntity.getValueMap(), pimsMapEntity.getDomainMap());
    }

    private <T extends PimsMapEntity> T fromValueMap(ParsedType type, Map<String, Object> valueMap, boolean mutable) {
        Class actualType = type.getActualType().get();
        Map<String, Object> domainMap = mapEntityTransformer.transform(type, valueMap, (mapEntityParsedType, value) -> {
            //noinspection unchecked
            Map<String, Object> castedValue = (Map<String, Object>) value;
            return fromValueMap(mapEntityParsedType, castedValue, mutable);
        });
        return proxy(mutable, actualType, valueMap, domainMap);

    }

    private <T extends PimsMapEntity> T proxy(boolean mutable, Class actualType, Map<String, Object> valueMap, Map<String, Object> domainMap) {
        //noinspection unchecked
        return (T) newProxyInstance(
                PimsFactory.class.getClassLoader(),
                new Class[]{actualType},
                new PimsEntityProxy(
                        actualType,
                        valueMap,
                        domainMap,
                        pimsLinker.link(actualType),
                        mutable
                )
        );
    }
}
