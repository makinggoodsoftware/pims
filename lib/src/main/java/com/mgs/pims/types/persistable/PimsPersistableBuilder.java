package com.mgs.pims.types.persistable;

import com.mgs.pims.annotations.PimsEntity;
import com.mgs.pims.types.builder.PimsBuilder;
import com.mgs.pims.types.entity.PimsMapEntity;

@PimsEntity
public interface PimsPersistableBuilder
        <
                M extends PimsMapEntity,
                P extends PimsPersistable<M>
        >
        extends
        PimsBuilder<P>
{
    PimsPersistableBuilder<M, P> withData (M data);
}
