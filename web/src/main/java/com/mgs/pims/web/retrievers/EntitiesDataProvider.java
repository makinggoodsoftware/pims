package com.mgs.pims.web.retrievers;

import com.mgs.pims.annotations.PimsEntity;
import com.mgs.pims.annotations.PimsMixer;
import com.mgs.pims.core.Pims;
import com.mgs.pims.types.metaData.PimsEntityMetaData;
import com.mgs.pims.types.provider.PimsProvider;

import java.util.List;

@PimsMixer
@PimsEntity(managedBy = EntitiesDataProvider.class)
public class EntitiesDataProvider implements PimsProvider<PimsEntityMetaData> {
    private final Pims pims;

    public EntitiesDataProvider(Pims pims) {
        this.pims = pims;
    }

    @Override
    public List<PimsEntityMetaData> get() {
        return null;
    }
}
