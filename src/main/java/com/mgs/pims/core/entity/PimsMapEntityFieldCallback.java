package com.mgs.pims.core.entity;

import com.mgs.maps.MapFieldValueFactory;
import com.mgs.maps.OnMapFieldCallback;
import com.mgs.pims.core.entity.PimsMapEntity;
import com.mgs.reflections.FieldAccessor;

import java.util.Map;

class PimsMapEntityFieldCallback implements OnMapFieldCallback {
	private final OnMapFieldCallback onSpecialCaseProcessor;
    private final Map<String, Object> transformed;
	private final MapFieldValueFactory mapFieldValueFactory;

    public PimsMapEntityFieldCallback(OnMapFieldCallback onSpecialCaseProcessor, Map<String, Object> transformed, MapFieldValueFactory mapFieldValueFactory) {
		this.onSpecialCaseProcessor = onSpecialCaseProcessor;
        this.transformed = transformed;
		this.mapFieldValueFactory = mapFieldValueFactory;
	}

    @Override
	public Object apply(FieldAccessor fieldAccessor, Object mapValue) {
		try {
		    Object value = mapFieldValueFactory.transform(
                    fieldAccessor,
					fieldAccessor.getReturnType(),
                    mapValue,
                    PimsMapEntity.class,
                    onSpecialCaseProcessor
			);
			transformed.put(
					fieldAccessor.getFieldName(),
					value
			);
            return value;
		} catch (Exception e) {
			throw new IllegalArgumentException("Error processing value for field: " + fieldAccessor, e);
		}
	}
}
