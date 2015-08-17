package com.mgs.pims.web.data;

import com.mgs.pims.annotations.PimsEntity;
import com.mgs.pims.types.map.PimsMapEntity;

@PimsEntity
public interface PersonData extends PimsMapEntity {
    String getName();
}
