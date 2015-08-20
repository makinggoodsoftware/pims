package com.mgs.pims.core.metaData;

import com.mgs.pims.types.ProxyFactory;
import com.mgs.pims.types.metaData.PimsEntityMetaData;
import com.mgs.pims.types.metaData.PimsEntityMetaDataBuilder;
import com.mgs.reflections.FieldAccessor;
import com.mgs.reflections.FieldAccessorParser;
import com.mgs.reflections.FieldAccessorType;
import com.mgs.reflections.ParsedType;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MetaDataFactory {
    private final FieldAccessorParser fieldAccessorParser;
    private final ProxyFactory proxyFactory;
    private final ParsedType metaDataGetterType;
    private final ParsedType metaDataBuilderType;

    public MetaDataFactory(FieldAccessorParser fieldAccessorParser, ProxyFactory proxyFactory, ParsedType metaDataGetterType, ParsedType metaDataBuilderType) {
        this.fieldAccessorParser = fieldAccessorParser;
        this.proxyFactory = proxyFactory;
        this.metaDataGetterType = metaDataGetterType;
        this.metaDataBuilderType = metaDataBuilderType;
    }

    public PimsEntityMetaData metadata(ParsedType entityType) {
        Map<String, FieldAccessor> fieldAccessors = fieldAccessorParser.parse(entityType).
                filter(
                        (fieldAccessor -> fieldAccessor.getType() == FieldAccessorType.GET)
                ).
                collect(Collectors.toMap(
                        FieldAccessor::getFieldName,
                        (fieldAccessor) -> fieldAccessor
                ));
        PimsEntityMetaDataBuilder pimsEntityMetaDataBuilder = proxyFactory.mutable(
                metaDataGetterType,
                metaDataBuilderType,
                new HashMap<>(),
                new HashMap<>(),
                null
        );
        pimsEntityMetaDataBuilder.withFields(fieldAccessors);
        pimsEntityMetaDataBuilder.withName(metaDataGetterType.getActualType().get().getSimpleName());
        pimsEntityMetaDataBuilder.withType(entityType);
        return pimsEntityMetaDataBuilder.build();
    }
}
