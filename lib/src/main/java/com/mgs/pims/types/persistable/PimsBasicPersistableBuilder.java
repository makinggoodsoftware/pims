package com.mgs.pims.types.persistable;

import com.mgs.pims.annotations.PimsEntity;
import com.mgs.pims.types.map.PimsMapEntity;

@PimsEntity
public interface PimsBasicPersistableBuilder extends PimsPersistableBuilder<PimsMapEntity, PimsPersistable<PimsMapEntity>> {
}
