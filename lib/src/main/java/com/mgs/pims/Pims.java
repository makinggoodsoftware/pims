package com.mgs.pims;

import com.mgs.pims.proxy.PimsEntityProxyFactory;
import com.mgs.pims.types.PimsFactory;
import com.mgs.pims.types.builder.PimsBuilder;
import com.mgs.pims.types.map.PimsMapEntity;
import com.mgs.pims.types.persistable.PimsPersistable;
import com.mgs.pims.types.persistable.PimsPersistableBuilder;
import com.mgs.pims.types.base.PimsBaseEntity;
import com.mgs.reflections.TypeParser;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Pims {
    private final PimsFactory pimsFactory;
    private final PimsEntityProxyFactory pimsEntityProxyFactory;
    private final TypeParser typeParser;

    public Pims(PimsFactory pimsFactory, PimsEntityProxyFactory pimsEntityProxyFactory, TypeParser typeParser) {
        this.pimsFactory = pimsFactory;
        this.pimsEntityProxyFactory = pimsEntityProxyFactory;
        this.typeParser = typeParser;
    }

    public <M extends PimsMapEntity> M
    newEntity (Class<M> type, Map<String, Object> value){
        return pimsFactory.immutable(type, value);
    }

    public <M extends PimsMapEntity, B extends PimsBuilder<M>> B
    newBuilder(Class<B> type){
        return pimsFactory.mutable(type, new HashMap<>(), new HashMap<>());
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

    public <T extends PimsBaseEntity> T stateless(Class<T> statelessType) {
        return pimsEntityProxyFactory.proxy(
                false,
                typeParser.parse(statelessType),
                new HashMap<>(),
                new HashMap<>()
        );
    }
}
