package com.mgs.pims.types.retriever;

import com.mgs.pims.annotations.PimsEntity;
import com.mgs.pims.types.map.PimsMapEntity;
import com.mgs.pims.types.persistable.PimsPersistable;
import com.mgs.pims.types.base.PimsBaseEntity;
import com.mgs.pims.types.serializable.PimsSerializable;

import java.util.List;

@PimsEntity(managedBy = PimsRetrievers.class)
public interface PimsRetriever<Z extends PimsSerializable, T extends PimsPersistable<Z>> extends PimsBaseEntity {
    List<T> byField(String fieldName, Object withValue);
}
