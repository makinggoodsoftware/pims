package com.mgs.pims;

import com.mgs.maps.MapUtils;
import com.mgs.pims.types.PimsFactory;
import com.mgs.pims.types.base.PimsBaseEntity;
import com.mgs.pims.types.builder.PimsBuilder;
import com.mgs.pims.types.map.PimsMapEntity;
import com.mgs.pims.types.persistable.PimsPersistable;
import com.mgs.pims.types.persistable.PimsPersistableBuilder;
import com.mgs.reflections.Declaration;
import com.mgs.reflections.ParsedType;
import com.mgs.reflections.TypeParser;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Pims {
    private final PimsFactory pimsFactory;
    private final TypeParser typeParser;
    private final MapUtils mapUtils;

    public Pims(PimsFactory pimsFactory, TypeParser typeParser, MapUtils mapUtils) {
        this.pimsFactory = pimsFactory;
        this.typeParser = typeParser;
        this.mapUtils = mapUtils;
    }

    public <M extends PimsMapEntity> M
    newEntity (Class<M> type, Map<String, Object> value){
        return pimsFactory.immutable(typeParser.parse(type), value);
    }

    public <M extends PimsMapEntity, B extends PimsBuilder<M>> B
    newBuilder(Class<B> type){
        ParsedType builderType = typeParser.parse(type);
        ParsedType getterType = getterType(builderType);
        return pimsFactory.mutable(
                getterType,
                builderType,
                new HashMap<>(),
                new HashMap<>()
        );
    }

    public <T extends PimsBaseEntity> T stateless(Class<T> statelessType) {
        return pimsFactory.immutable(
                typeParser.parse(statelessType),
                new HashMap<>()
        );
    }

    public <
            M extends PimsMapEntity,
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
        Map valueMapCopy = mapUtils.copy(source.getValueMap());
        Map domainMapCopy = mapUtils.copy(source.getDomainMap());
        //noinspection unchecked
        return (B) pimsFactory.mutable(
                getterType(type),
                type,
                valueMapCopy,
                domainMapCopy
        );
    }

    private ParsedType getterType(ParsedType baseBuilderType) {
        ParsedType builderType = baseBuilderType.getSuperDeclarations().get(PimsBuilder.class);
        Declaration getterDeclaration = builderType.getOwnDeclaration().getParameters().get("T");
        return typeParser.parse(getterDeclaration);
    }
}
