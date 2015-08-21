package com.mgs.pims.types;

import com.mgs.maps.MapTransformer;
import com.mgs.pims.core.linker.PimsLink;
import com.mgs.pims.core.linker.PimsLinker;
import com.mgs.pims.core.linker.method.PimsMethodCaller;
import com.mgs.pims.core.linker.method.PimsMethodDelegator;
import com.mgs.pims.core.linker.parameters.PimsParameters;
import com.mgs.pims.proxy.PimsEntityProxy;
import com.mgs.pims.types.base.PimsBaseEntity;
import com.mgs.pims.types.metaData.PimsEntityMetaData;
import com.mgs.reflections.FieldAccessorParser;
import com.mgs.reflections.ParsedType;

import java.util.Map;

import static com.mgs.pims.event.PimsEventType.INPUT_TRANSLATION;
import static java.lang.reflect.Proxy.newProxyInstance;

public class ProxyFactory {
    private final MapTransformer mapTransformer;
    private final PimsParameters pimsParameters;
    private final PimsLinker pimsLinker;
    private final PimsMethodCaller pimsMethodCaller;

    public ProxyFactory(MapTransformer mapTransformer, PimsParameters pimsParameters, PimsLinker pimsLinker, PimsMethodCaller pimsMethodCaller, FieldAccessorParser fieldAccessorParser, ParsedType metaDataGetterType, ParsedType metaDataBuilderType) {
        this.mapTransformer = mapTransformer;
        this.pimsParameters = pimsParameters;
        this.pimsLinker = pimsLinker;
        this.pimsMethodCaller = pimsMethodCaller;
    }

    public <T extends PimsBaseEntity> T immutable(
            ParsedType type,
            Map<String, Object> valueMap,
            PimsEntityMetaData pimsEntityMetaData
    ) {
        return fromValueMap(
                type,
                type,
                valueMap,
                false,
                pimsEntityMetaData
        );
    }

    public <T extends PimsBaseEntity> T mutable(
            ParsedType getterType,
            ParsedType type,
            Map<String, Object> valueMap,
            Map<String, Object> domainMap,
            PimsEntityMetaData pimsEntityMetaData
    ) {
        return withMutability(
                getterType,
                type,
                valueMap,
                domainMap,
                true,
                pimsLinker.link(type.getActualType().get()),
                pimsEntityMetaData
        );
    }

    public <T extends PimsBaseEntity> T domainEntity(
            ParsedType type,
            Map<String, Object> domainMap,
            PimsEntityMetaData pimsEntityMetaData
    ) {
        return withMutability(
                type,
                type,
                null,
                domainMap,
                true,
                pimsLinker.link(type.getActualType().get()),
                pimsEntityMetaData
        );
    }

    private <T extends PimsBaseEntity> T fromValueMap(
            ParsedType getterType,
            ParsedType type,
            Map<String, Object> valueMap,
            boolean mutable,
            PimsEntityMetaData pimsEntityMetaData
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
                (childType, childMap) -> fromValueMap(getterType, childType, childMap, mutable, pimsEntityMetaData));
        //noinspection unchecked
        return (T) withMutability(
                getterType,
                type,
                valueMap,
                domainMap,
                mutable,
                link,
                pimsEntityMetaData
        );
    }

    private <T extends PimsBaseEntity> T withMutability(
            ParsedType getterType,
            ParsedType type,
            Map<String, Object> valueMap,
            Map<String, Object> domainMap,
            boolean mutable,
            PimsLink link,
            PimsEntityMetaData pimsEntityMetaData
    ) {
        return proxy(
                mutable,
                getterType,
                type,
                valueMap,
                domainMap,
                link,
                pimsEntityMetaData
        );
    }

    private <T extends PimsBaseEntity> T proxy(
            boolean mutable,
            ParsedType getterType,
            ParsedType type,
            Map<String, Object> valueMap,
            Map<String, Object> domainMap,
            PimsLink link,
            PimsEntityMetaData pimsEntityMetaData
    ) {
        //noinspection unchecked
        return (T) newProxyInstance(
                ProxyFactory.class.getClassLoader(),
                new Class[]{type.getActualType().get()},
                new PimsEntityProxy(
                        pimsMethodCaller,
                        type,
                        domainMap,
                        valueMap,
                        link.getMethods(),
                        mutable,
                        pimsParameters,
                        pimsEntityMetaData
                )
        );
    }
}
