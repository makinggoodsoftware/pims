package com.pims.core;

import com.reflections.ParsedType;

import java.util.HashMap;
import java.util.Map;

public class MapEntityTransformer {
	private final MapWalker mapWalker;
	private final MapEntityFieldTransformer mapEntityFieldTransformer;

	public MapEntityTransformer(MapWalker mapWalker, MapEntityFieldTransformer mapEntityFieldTransformer) {
		this.mapWalker = mapWalker;
		this.mapEntityFieldTransformer = mapEntityFieldTransformer;
	}

	public Map<String, Object> transform(ParsedType type, Map<String, Object> entityMap, OnMapEntityProcessor onMapEntityProcessor) {
		Map<String, Object> transformed = new HashMap<>();
		mapWalker.walk(entityMap, type, (fieldAccessor, mapValue) -> {
			Object value = null;
			try {
				value = mapEntityFieldTransformer.transform(
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
