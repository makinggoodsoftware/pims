package com.mgs.pims.web.persistable;

import com.mgs.pims.annotations.PimsEntity;
import com.mgs.pims.types.persistable.PimsPersistable;
import com.mgs.pims.web.data.PersonData;

@PimsEntity
public interface Person extends PimsPersistable<PersonData> {
}
