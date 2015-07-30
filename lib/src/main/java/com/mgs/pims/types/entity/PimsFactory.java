package com.mgs.pims.types.entity;

import com.mgs.maps.MapFieldValueFactory;
import com.mgs.maps.MapWalker;
import com.mgs.maps.OnMapFieldCallback;
import com.mgs.pims.proxy.PimsEntityProxyFactory;
import com.mgs.reflections.ParsedType;
import com.mgs.reflections.TypeParser;

import java.util.HashMap;
import java.util.Map;

public class PimsFactory {
    private final PimsEntityProxyFactory pimsEntityProxyFactory;
    private final MapWalker mapWalker;
    private final MapFieldValueFactory mapFieldValueFactory;
    private final TypeParser typeParser;

    public PimsFactory(PimsEntityProxyFactory pimsEntityProxyFactory, MapWalker mapWalker, MapFieldValueFactory mapFieldValueFactory, TypeParser typeParser) {
        this.pimsEntityProxyFactory = pimsEntityProxyFactory;
        this.mapWalker = mapWalker;
        this.mapFieldValueFactory = mapFieldValueFactory;
        this.typeParser = typeParser;
    }

    public <T extends PimsMapEntity> T immutable (Class<T> type, Map<String, Object> valueMap){
        return immutable(typeParser.parse(type), valueMap);
    }

    public <T extends PimsMapEntity> T immutable(ParsedType type, Map<String, Object> valueMap) {
        return fromValueMap(type, valueMap, false);
    }

    private <T extends PimsMapEntity> T fromValueMap(ParsedType type, Map<String, Object> valueMap, boolean mutable) {
        Class actualType = type.getActualType().get();
        OnMapFieldCallback onSpecialCaseProcessor = (fieldAccessor, value) -> {
            //noinspection unchecked
            Map<String, Object> castedValue = (Map<String, Object>) value;
            return fromValueMap(fieldAccessor.getReturnType(), castedValue, mutable);
        };
        Map<String, Object> transformed = new HashMap<>();
        mapWalker.walk(type, valueMap, new PimsMapEntityFieldCallback(onSpecialCaseProcessor, transformed, mapFieldValueFactory));
        return pimsEntityProxyFactory.proxy(mutable, actualType, valueMap, transformed);

    }


}
