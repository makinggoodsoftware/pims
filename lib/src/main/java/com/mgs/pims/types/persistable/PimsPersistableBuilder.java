package com.mgs.pims.types.persistable;

import com.mgs.pims.annotations.PimsEntity;
import com.mgs.pims.types.builder.PimsBuilder;
import com.mgs.pims.types.map.PimsMapEntity;
import com.mgs.pims.types.serializable.PimsSerializable;

import java.util.function.Function;
import java.util.function.UnaryOperator;

@PimsEntity
public interface PimsPersistableBuilder
        <
                M extends PimsSerializable,
                P extends PimsPersistable<M>
        >
        extends
        PimsBuilder<P>
{
    PimsPersistableBuilder<M, P> withData (M data);

    PimsPersistableBuilder<M, P> updateVersion(UnaryOperator<Integer> versionUpdater);
}
