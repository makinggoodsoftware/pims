package com.mgs.pims;

import com.mgs.pims.types.PimsFactory;
import com.mgs.pims.types.base.PimsBaseEntity;
import com.mgs.pims.types.builder.PimsBuilder;
import com.mgs.pims.types.map.PimsMapEntity;
import com.mgs.pims.types.persistable.PimsPersistable;
import com.mgs.pims.types.persistable.PimsPersistableBuilder;
import com.mgs.reflections.TypeParser;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Pims {
    private final PimsFactory pimsFactory;
    private final TypeParser typeParser;

    public Pims(PimsFactory pimsFactory, TypeParser typeParser) {
        this.pimsFactory = pimsFactory;
        this.typeParser = typeParser;
    }

    public <M extends PimsMapEntity> M
    newEntity (Class<M> type, Map<String, Object> value){
        return pimsFactory.immutable(typeParser.parse(type), value);
    }

    public <M extends PimsMapEntity, B extends PimsBuilder<M>> B
    newBuilder(Class<B> type){
        return pimsFactory.mutable(typeParser.parse(type), new HashMap<>(), new HashMap<>());
    }

    public <T extends PimsBaseEntity> T stateless(Class<T> statelessType) {
        return pimsFactory.immutable(
                typeParser.parse(statelessType),
                new HashMap<>(),
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
}
