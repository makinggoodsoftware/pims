package com.mgs.pims;

import com.mgs.pims.types.PimsFactory;
import com.mgs.pims.types.builder.PimsBuilder;
import com.mgs.pims.types.entity.PimsMapEntity;

import java.util.HashMap;
import java.util.Map;

public class Pims {
    private final PimsFactory pimsFactory;

    public Pims(PimsFactory pimsFactory) {
        this.pimsFactory = pimsFactory;
    }

    public <T extends PimsMapEntity> T newEntity (Class<T> type, Map<String, Object> value){
        return pimsFactory.immutable(type, value);
    }

    public <Z extends PimsMapEntity, T extends PimsBuilder<Z>> T newBuilder(Class<T> type){
        return pimsFactory.mutable(type, new HashMap<>(), new HashMap<>());
    }
}
