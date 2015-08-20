package com.mgs.pims.types.provider;

import com.mgs.pims.annotations.PimsEntity;
import com.mgs.pims.types.base.PimsBaseEntity;
import com.mgs.pims.types.map.PimsMapEntity;

import java.util.List;

@PimsEntity
public interface PimsProvider<T extends PimsMapEntity> extends PimsBaseEntity {
    List<T> get();
}
