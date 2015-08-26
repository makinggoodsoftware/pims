package com.mgs.pims.context;

import com.mgs.maps.MapUtils;
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
    private final Map<String, PimsEntityDescriptor> descriptors;
    private final ProxyFactory proxyFactory;
    private final MapUtils mapUtils;
    private final TypeParser typeParser;

    public Pims(Map<String, PimsEntityDescriptor> descriptors, ProxyFactory proxyFactory, TypeParser typeParser, MapUtils mapUtils) {
        this.descriptors = descriptors;
        this.proxyFactory = proxyFactory;
        this.typeParser = typeParser;
        this.mapUtils = mapUtils;
    }

    public PimsEntityDescriptor get(String name) {
        return descriptors.get(name);
    }

    public PimsEntityDescriptor get(Class<? extends PimsBaseEntity> type) {
        return get(type.getSimpleName());
    }

    public PimsEntityDescriptor get(ParsedType type) {
        return get(type.getActualType().get());
    }

    public <M extends PimsSerializable> M
    newEntity(Class<M> type, Map<String, Object> valueMap) {
        PimsEntityDescriptor pimsEntityDescriptor = get(type);
        return proxyFactory.immutable(
                pimsEntityDescriptor.getStaticDescriptor().getType(),
                valueMap,
                pimsEntityDescriptor.getStaticDescriptor().getMetaData()
        );
    }

    public <M extends PimsMapEntity, B extends PimsBuilder<M>> B
    newBuilder(Class<B> type){
        PimsEntityDescriptor pimsEntityDescriptor = get(type);
        ParsedType builderType = pimsEntityDescriptor.getStaticDescriptor().getType();
        ParsedType getterType = getterType(builderType);
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

    public <T extends PimsBaseEntity> T stateless(Class<T> statelessType) {
        PimsEntityDescriptor pimsEntityDescriptor = get(statelessType);
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
    PB newPersistedEntityBuilder(Class<PB> persistableBuilderType, Class<B> dataBuilderType, Function<B, B> builderFn) {
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
    B update(Class<B> builder, M source) {
        return update(
                typeParser.parse(builder),
                source
        );
    }

    public <
            M extends PimsMapEntity,
            B extends PimsBuilder<M>
            >
    B update(ParsedType type, M source) {
        PimsEntityDescriptor pimsEntityDescriptor = get(type);
        Map domainMapCopy = mapUtils.copy(source.getDomainMap());
        //noinspection unchecked
        return (B) proxyFactory.mutable(
                getterType(type),
                type,
                new HashMap<>(),
                domainMapCopy,
                pimsEntityDescriptor.getStaticDescriptor().getMetaData()
        );
    }

    private ParsedType getterType(ParsedType baseBuilderType) {
        ParsedType builderType = baseBuilderType.getSuperDeclarations().get(PimsBuilder.class);
        Declaration getterDeclaration = builderType.getOwnDeclaration().getParameters().get("T");
        return typeParser.parse(getterDeclaration);
    }
}
