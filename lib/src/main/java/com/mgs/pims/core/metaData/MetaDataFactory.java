package com.mgs.pims.core.metaData;

import com.mgs.pims.types.ProxyFactory;
import com.mgs.pims.types.metaData.PimsEntityMetaData;
import com.mgs.pims.types.metaData.PimsEntityMetaDataBuilder;
import com.mgs.reflections.FieldAccessor;
import com.mgs.reflections.FieldAccessorParser;
import com.mgs.reflections.ParsedType;

import java.util.HashMap;
import java.util.Map;

public class MetaDataFactory {
    private final FieldAccessorParser fieldAccessorParser;
    private final ProxyFactory proxyFactory;
    private final ParsedType metaDataGetterType;
    private final ParsedType metaDataBuilderType;
    private final PimsEntityMetaData metaMetaData;

    public MetaDataFactory(FieldAccessorParser fieldAccessorParser, ProxyFactory proxyFactory, ParsedType metaDataGetterType, ParsedType metaDataBuilderType, PimsEntityMetaData metaMetaData) {
        this.fieldAccessorParser = fieldAccessorParser;
        this.proxyFactory = proxyFactory;
        this.metaDataGetterType = metaDataGetterType;
        this.metaDataBuilderType = metaDataBuilderType;
        this.metaMetaData = metaMetaData;
    }

    public PimsEntityMetaData metadata(ParsedType entityType) {
        Map<String, FieldAccessor> fieldAccessors = fieldAccessorParser.asMap(entityType);
        PimsEntityMetaDataBuilder pimsEntityMetaDataBuilder = proxyFactory.mutable(
                metaDataGetterType,
                metaDataBuilderType,
                new HashMap<>(),
                new HashMap<>(),
                metaMetaData
        );
        pimsEntityMetaDataBuilder.withFields(fieldAccessors);
        pimsEntityMetaDataBuilder.withName(metaDataGetterType.getActualType().get().getSimpleName());
        pimsEntityMetaDataBuilder.withType(entityType);
        return pimsEntityMetaDataBuilder.build();
    }

}
