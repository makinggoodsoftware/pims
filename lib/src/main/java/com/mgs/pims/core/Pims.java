package com.mgs.pims.core;

import com.mgs.maps.MapUtils;
import com.mgs.pims.context.PimsEntityDescriptor;
import com.mgs.pims.types.ProxyFactory;
import com.mgs.pims.types.base.PimsBaseEntity;
import com.mgs.pims.types.builder.PimsBuilder;
import com.mgs.pims.types.map.PimsMapEntity;
import com.mgs.pims.types.metaData.PimsEntityMetaData;
import com.mgs.pims.types.persistable.PimsPersistable;
import com.mgs.pims.types.persistable.PimsPersistableBuilder;
import com.mgs.pims.types.serializable.PimsSerializable;
import com.mgs.reflections.Declaration;
import com.mgs.reflections.ParsedType;
import com.mgs.reflections.TypeParser;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Pims {
    private final ProxyFactory proxyFactory;
    private final MapUtils mapUtils;
    private final TypeParser typeParser;

    public Pims(ProxyFactory proxyFactory, TypeParser typeParser, MapUtils mapUtils) {
        this.proxyFactory = proxyFactory;
        this.typeParser = typeParser;
        this.mapUtils = mapUtils;
    }

    public <M extends PimsSerializable> M
    newEntity(PimsEntityDescriptor pimsEntityDescriptor, Map<String, Object> valueMap) {
        return proxyFactory.immutable(
                pimsEntityDescriptor.getStaticDescriptor().getType(),
                valueMap,
                pimsEntityDescriptor.getStaticDescriptor().getMetaData()
        );
    }

    public <M extends PimsMapEntity, B extends PimsBuilder<M>> B
    newBuilder(PimsEntityDescriptor pimsEntityDescriptor) {
        ParsedType builderType = pimsEntityDescriptor.getStaticDescriptor().getType();
        ParsedType getterType = getterType(pimsEntityDescriptor);
        PimsEntityMetaData metaData = pimsEntityDescriptor.getStaticDescriptor().getMetaData();
        if (metaData == null) throw new NullPointerException();
        return proxyFactory.mutable(
                getterType,
                builderType,
                new HashMap<>(),
                new HashMap<>(),
                metaData
        );
    }

    public <T extends PimsBaseEntity> T stateless(PimsEntityDescriptor pimsEntityDescriptor) {
        return proxyFactory.immutable(
                pimsEntityDescriptor.getStaticDescriptor().getType(),
                new HashMap<>(),
                pimsEntityDescriptor.getStaticDescriptor().getMetaData()
        );
    }

    public <
            M extends PimsSerializable,
            B extends PimsBuilder<M>,
            P extends PimsPersistable<M>,
            PB extends PimsPersistableBuilder<M, P>
            >
    PB newPersistedEntityBuilder(PimsEntityDescriptor persistableBuilderType, PimsEntityDescriptor dataBuilderType, Function<B, B> builderFn) {
        B dataBuilder = newBuilder(dataBuilderType);
        M data = builderFn.apply(dataBuilder).build();
        PB persistableBuilder = newBuilder(persistableBuilderType);
        persistableBuilder.withData(data);
        return persistableBuilder;
    }

    public <
            M extends PimsMapEntity,
            B extends PimsBuilder<M>
            >
    B update(PimsEntityDescriptor pimsEntityDescriptor, M source) {
        Map domainMapCopy = mapUtils.copy(source.getDomainMap());
        //noinspection unchecked
        return (B) proxyFactory.mutable(
                getterType(pimsEntityDescriptor),
                pimsEntityDescriptor.getStaticDescriptor().getType(),
                new HashMap<>(),
                domainMapCopy,
                pimsEntityDescriptor.getStaticDescriptor().getMetaData()
        );
    }

    private ParsedType getterType(PimsEntityDescriptor descriptor) {
        ParsedType baseBuilderType = descriptor.getStaticDescriptor().getType();
        ParsedType builderType = baseBuilderType.getSuperDeclarations().get(PimsBuilder.class);
        Declaration getterDeclaration = builderType.getOwnDeclaration().getParameters().get("T");
        return typeParser.parse(getterDeclaration);
    }
}
