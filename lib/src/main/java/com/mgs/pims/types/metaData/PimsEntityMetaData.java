package com.mgs.pims.types.metaData;

import com.mgs.pims.annotations.PimsEntity;
import com.mgs.pims.types.map.PimsMapEntity;
import com.mgs.reflections.FieldAccessor;
import com.mgs.reflections.ParsedType;

import java.util.Map;

@PimsEntity
public interface PimsEntityMetaData extends PimsMapEntity {
    String getName();

    ParsedType getType();

    Map<String, FieldAccessor> getFields();
}
