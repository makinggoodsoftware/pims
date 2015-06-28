package com.pims.core;

import com.reflections.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Stream.of;


public class MapWalker {
	private final FieldAccessorParser fieldAccessorParser;
	private final BeanNamingExpert beanNamingExpert;
	private final Reflections reflections;
	private final TypeParser typeParser;

	public MapWalker(FieldAccessorParser fieldAccessorParser, BeanNamingExpert beanNamingExpert, Reflections reflections, TypeParser typeParser) {
		this.fieldAccessorParser = fieldAccessorParser;
		this.beanNamingExpert = beanNamingExpert;
		this.reflections = reflections;
		this.typeParser = typeParser;
	}

	public void walk(Map<String, Object> map, Class<? extends PimsMapEntity> type, OnMapFieldCallback callback) {
		walk(map, typeParser.parse(type), callback);
	}

	public void walk(Map<String, Object> map, ParsedType type, OnMapFieldCallback callback) {
		Map<String, List<FieldAccessor>> accesorsByMethodName =
				fieldAccessorParser.parse(type).
						filter(this::isAGetter).
						collect(Collectors.groupingBy(FieldAccessor::getMethodName));

		accesorsByMethodName.entrySet().stream().
        filter(accessorByMethodNameEntry -> {
            Collection<FieldAccessor> accessors = accessorByMethodNameEntry.getValue();
            if (accessors.size() != 1) throw new IllegalStateException("There seems to be more than one accessor for: " + accessorByMethodNameEntry.getKey());

            FieldAccessor accessor = accessors.iterator().next();
			return of(accessor.getAnnotations()).
					filter(annotation -> annotation.annotationType().equals(VirtualField.class)
					).count() == 0;
        }).
		forEach((accessorByMethodNameEntry) -> {
            Collection<FieldAccessor> accessors = accessorByMethodNameEntry.getValue();

            FieldAccessor accessor = accessors.iterator().next();
            String fieldName = extractFieldName(accessor);
            callback.apply(accessor, map.get(fieldName));
        });
	}

	private String extractFieldName(FieldAccessor accessor) {
		Optional<Mapping> fieldNameOptional = reflections.annotation(accessor.getAnnotations(), Mapping.class);
		if (fieldNameOptional.isPresent()) return fieldNameOptional.get().mapFieldName();
		return beanNamingExpert.getFieldName(accessor.getMethodName(), "get");
	}

	private boolean isAGetter(FieldAccessor accessor) {
		return accessor.getType() == FieldAccessorType.GET;
	}
}
