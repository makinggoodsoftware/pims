package com.mgs.pims.types.resource;

import com.mgs.maps.VirtualField;
import com.mgs.pims.annotations.PimsEntity;
import com.mgs.pims.types.entity.PimsMapEntity;

import java.util.Optional;

@PimsEntity(managedBy = PimsResources.class)
public interface PimsResource <T extends PimsMapEntity> extends PimsMapEntity {
    T getData();

    @VirtualField
    Optional<Integer> getVersion();

    PimsResource<T> persist ();
}
