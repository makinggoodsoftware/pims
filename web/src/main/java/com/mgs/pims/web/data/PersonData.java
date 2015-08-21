package com.mgs.pims.web.data;

import com.mgs.pims.annotations.PimsEntity;
import com.mgs.pims.types.map.PimsMapEntity;
import com.mgs.pims.types.serializable.PimsSerializable;

@PimsEntity
public interface PersonData extends PimsSerializable {
    String getName();
}
