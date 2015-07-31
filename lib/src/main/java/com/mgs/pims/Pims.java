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

    private <T extends PimsMapEntity> T newEntity (Class<T> type, Map<String, Object> value){
        return pimsFactory.immutable(type, value);
    }

    private <Z extends PimsMapEntity, T extends PimsBuilder<Z>> T newBuilder (Class<T> type, Map<String, Object> value){
        return pimsFactory.mutable(type, new HashMap<>(), new HashMap<>());
    }
}
