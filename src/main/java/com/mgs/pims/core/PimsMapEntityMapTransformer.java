package com.mgs.pims.core;

import com.reflections.ParsedType;

import java.util.HashMap;
import java.util.Map;

public class PimsMapEntityMapTransformer {
	private final MapWalker mapWalker;
	private final PimsMapEntityFieldValueFactory pimsMapEntityFieldValueFactory;

	public PimsMapEntityMapTransformer(MapWalker mapWalker, PimsMapEntityFieldValueFactory pimsMapEntityFieldValueFactory) {
		this.mapWalker = mapWalker;
		this.pimsMapEntityFieldValueFactory = pimsMapEntityFieldValueFactory;
	}

	public Map<String, Object> transform(ParsedType type, Map<String, Object> entityMap, OnMapEntityProcessor onMapEntityProcessor) {
		Map<String, Object> transformed = new HashMap<>();
		mapWalker.walk(entityMap, type, (fieldAccessor, mapValue) -> {
			Object value = null;
			try {
				value = pimsMapEntityFieldValueFactory.transform(
                        fieldAccessor.getReturnType(),
                        mapValue,
                        onMapEntityProcessor
                );
			} catch (Exception e) {
				throw new IllegalArgumentException("Error processing value for field: " + fieldAccessor, e);
			}
			transformed.put(
					fieldAccessor.getFieldName(),
					value
			);
		});
		return transformed;
	}
}
