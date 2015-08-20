package com.mgs.pims.types;

import com.mgs.maps.MapTransformer;
import com.mgs.pims.core.linker.PimsLink;
import com.mgs.pims.core.linker.PimsLinker;
import com.mgs.pims.core.linker.method.PimsMethodCaller;
import com.mgs.pims.core.linker.method.PimsMethodDelegator;
import com.mgs.pims.core.linker.parameters.PimsParameters;
import com.mgs.pims.proxy.PimsEntityProxy;
import com.mgs.pims.types.base.PimsBaseEntity;
import com.mgs.pims.types.metaData.PimsEntityMetaDataBuilder;
import com.mgs.reflections.FieldAccessor;
import com.mgs.reflections.FieldAccessorParser;
import com.mgs.reflections.FieldAccessorType;
import com.mgs.reflections.ParsedType;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mgs.pims.event.PimsEventType.INPUT_TRANSLATION;
import static java.lang.reflect.Proxy.newProxyInstance;

public class PimsFactory {
    private final MapTransformer mapTransformer;
    private final PimsParameters pimsParameters;
    private final PimsLinker pimsLinker;
    private final PimsMethodCaller pimsMethodCaller;
    private final FieldAccessorParser fieldAccessorParser;
    private final ParsedType metaDataGetterType;
    private final ParsedType metaDataBuilderType;

    public PimsFactory(MapTransformer mapTransformer, PimsParameters pimsParameters, PimsLinker pimsLinker, PimsMethodCaller pimsMethodCaller, FieldAccessorParser fieldAccessorParser, ParsedType metaDataGetterType, ParsedType metaDataBuilderType) {
        this.mapTransformer = mapTransformer;
        this.pimsParameters = pimsParameters;
        this.pimsLinker = pimsLinker;
        this.pimsMethodCaller = pimsMethodCaller;
        this.fieldAccessorParser = fieldAccessorParser;
        this.metaDataGetterType = metaDataGetterType;
        this.metaDataBuilderType = metaDataBuilderType;
    }

    public <T extends PimsBaseEntity> T immutable(ParsedType type, Map<String, Object> valueMap) {
        return fromValueMap(
                type,
                type,
                valueMap,
                false
        );
    }

    public <T extends PimsBaseEntity> T mutable(ParsedType getterType, ParsedType type, Map<String, Object> valueMap, Map<String, Object> domainMap) {
        return withMutability(
                getterType,
                type,
                valueMap,
                domainMap,
                true,
                pimsLinker.link(type.getActualType().get()
                ));
    }

    private <T extends PimsBaseEntity> T fromValueMap(
            ParsedType getterType,
            ParsedType type,
            Map<String, Object> valueMap,
            boolean mutable
    ) {
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
                (childType, childMap) -> fromValueMap(getterType, childType, childMap, mutable));
        //noinspection unchecked
        return (T) withMutability(
                getterType,
                type,
                valueMap,
                domainMap,
                mutable,
                link
        );
    }

    private <T extends PimsBaseEntity> T withMutability(
            ParsedType getterType,
            ParsedType type,
            Map<String, Object> valueMap,
            Map<String, Object> domainMap,
            boolean mutable,
            PimsLink link
    ) {
        return proxy(
                mutable,
                getterType,
                type,
                valueMap,
                domainMap,
                link
        );
    }

    private <T extends PimsBaseEntity> T proxy(
            boolean mutable,
            ParsedType getterType,
            ParsedType type,
            Map<String, Object> valueMap,
            Map<String, Object> domainMap,
            PimsLink link
    ) {
        Map<String, FieldAccessor> fieldAccessors = fieldAccessorParser.parse(getterType).
                filter(
                        (fieldAccessor -> fieldAccessor.getType() == FieldAccessorType.GET)
                ).
                collect(Collectors.toMap(
                        FieldAccessor::getFieldName,
                        (fieldAccessor) -> fieldAccessor
                ));
        PimsEntityMetaDataBuilder pimsEntityMetaDataBuilder = mutable(metaDataGetterType, metaDataBuilderType, new HashMap<>(), new HashMap<>());
        pimsEntityMetaDataBuilder.withFields(fieldAccessors);
        pimsEntityMetaDataBuilder.withName(metaDataGetterType.getActualType().get().getSimpleName());
        pimsEntityMetaDataBuilder.withType(type);
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
                        pimsParameters,
                        fieldAccessors,
                        pimsEntityMetaDataBuilder.build())
        );
    }
}
