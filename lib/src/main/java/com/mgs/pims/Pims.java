package com.mgs.pims;

import com.mgs.pims.types.PimsFactory;
import com.mgs.pims.types.builder.PimsBuilder;
import com.mgs.pims.types.entity.PimsMapEntity;
import com.mgs.pims.types.persistable.PimsPersistable;
import com.mgs.pims.types.persistable.PimsPersistableBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Pims {
    private final PimsFactory pimsFactory;

    public Pims(PimsFactory pimsFactory) {
        this.pimsFactory = pimsFactory;
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
}
