package com.mgs.pims.types.entity;

import com.mgs.maps.MapTransformer;
import com.mgs.pims.proxy.PimsEntityProxyFactory;
import com.mgs.reflections.ParsedType;
import com.mgs.reflections.TypeParser;

import java.util.Map;

public class PimsFactory {
    private final MapTransformer mapTransformer;
    private final PimsEntityProxyFactory pimsEntityProxyFactory;
    private final TypeParser typeParser;

    public PimsFactory(MapTransformer mapTransformer, PimsEntityProxyFactory pimsEntityProxyFactory, TypeParser typeParser) {
        this.mapTransformer = mapTransformer;
        this.pimsEntityProxyFactory = pimsEntityProxyFactory;
        this.typeParser = typeParser;
    }

    public <T extends PimsMapEntity> T immutable (Class<T> type, Map<String, Object> valueMap){
        return immutable(typeParser.parse(type), valueMap);
    }

    public <T extends PimsMapEntity> T immutable(ParsedType type, Map<String, Object> valueMap) {
        return fromValueMap(type, valueMap, false);
    }

    private <T extends PimsMapEntity> T fromValueMap(ParsedType type, Map<String, Object> valueMap, boolean mutable) {
        return pimsEntityProxyFactory.proxy(
                mutable,
                type.getActualType().get(),
                valueMap,
                mapTransformer.objectify(
                        type,
                        valueMap,
                        (childType, childMap) -> fromValueMap(childType, childMap, mutable))
                );

    }
}
