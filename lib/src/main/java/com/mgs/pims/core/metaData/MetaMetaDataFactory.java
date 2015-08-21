package com.mgs.pims.core.metaData;

import com.mgs.maps.VirtualField;
import com.mgs.pims.annotations.PimsEntity;
import com.mgs.pims.types.metaData.PimsEntityMetaData;
import com.mgs.reflections.FieldAccessor;
import com.mgs.reflections.FieldAccessorParser;
import com.mgs.reflections.ParsedType;

import java.util.HashMap;
import java.util.Map;

public class MetaMetaDataFactory {
    private final FieldAccessorParser fieldAccessorParser;

    public MetaMetaDataFactory(FieldAccessorParser fieldAccessorParser) {
        this.fieldAccessorParser = fieldAccessorParser;
    }


    public PimsEntityMetaData metaMetadata(ParsedType metaDataType) {
        return new MyPimsEntityMetaData(metaDataType);
    }

    @PimsEntity
    private class MyPimsEntityMetaData implements PimsEntityMetaData {
        private final ParsedType metaDataType;

        public MyPimsEntityMetaData(ParsedType metaDataType) {
            this.metaDataType = metaDataType;
        }

        @Override
        @VirtualField
        public String getName() {
            return "metaMetaData";
        }

        @Override
        @VirtualField
        public ParsedType getType() {
            return metaDataType;
        }

        @Override
        @VirtualField
        public Map<String, FieldAccessor> getFields() {
            return fieldAccessorParser.asMap(metaDataType);
        }

        @Override
        @VirtualField
        public Map<String, Object> getValueMap() {
            return new HashMap<>();
        }

        @Override
        @VirtualField
        public Map<String, Object> getDomainMap() {
            return new HashMap<>();
        }

        @Override
        @VirtualField
        public boolean isMutable() {
            return false;
        }

        @Override
        @VirtualField
        public PimsEntityMetaData getMetaData() {
            return this;
        }
    }
}
