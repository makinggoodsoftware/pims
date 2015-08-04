package com.mgs.pims.types.retriever;

import com.mgs.pims.annotations.PimsEntity;
import com.mgs.pims.types.map.PimsMapEntity;
import com.mgs.pims.types.persistable.PimsPersistable;
import com.mgs.pims.types.base.PimsBaseEntity;

@PimsEntity(managedBy = PimsRetrievers.class)
public interface PimsRetriever<Z extends PimsMapEntity, T extends PimsPersistable<Z>> extends PimsBaseEntity {
}
