package com.mgs.pims.types.metaData;

import com.mgs.pims.annotations.PimsEntity;
import com.mgs.pims.types.builder.PimsBuilder;
import com.mgs.pims.types.map.PimsMapEntity;
import com.mgs.reflections.FieldAccessor;
import com.mgs.reflections.ParsedType;

import java.util.Map;

@PimsEntity
public interface PimsEntityMetaDataBuilder extends PimsBuilder<PimsEntityMetaData> {
    PimsEntityMetaDataBuilder withName(String name);

    PimsEntityMetaDataBuilder withType(ParsedType type);

    PimsEntityMetaDataBuilder withFields(Map<String, FieldAccessor> fields);
}
