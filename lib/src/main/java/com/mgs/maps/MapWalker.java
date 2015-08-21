package com.mgs.maps;

import com.mgs.reflections.*;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Stream.of;


public class MapWalker {
	private final FieldAccessorParser fieldAccessorParser;
	private final BeanNamingExpert beanNamingExpert;
	private final Reflections reflections;

	public MapWalker(FieldAccessorParser fieldAccessorParser, BeanNamingExpert beanNamingExpert, Reflections reflections) {
		this.fieldAccessorParser = fieldAccessorParser;
		this.beanNamingExpert = beanNamingExpert;
		this.reflections = reflections;
	}

	public void walk(ParsedType type, Map<String, Object> map, OnMapField callback) {
		Map<String, List<FieldAccessor>> accesorsByMethodName =
				fieldAccessorParser.parse(type).
						filter(this::isAGetter).
						collect(Collectors.groupingBy(FieldAccessor::getMethodName));

		accesorsByMethodName.entrySet().stream().
        filter(accessorByMethodNameEntry -> {
            Collection<FieldAccessor> accessors = accessorByMethodNameEntry.getValue();
            if (accessors.size() != 1) throw new IllegalStateException("There seems to be more than one accessor for: " + accessorByMethodNameEntry.getKey());

            FieldAccessor accessor = accessors.iterator().next();
			boolean notObjectMethod = ! accessor.getMethod().getDeclaringClass().equals(Object.class);
			boolean notVirtualField = of(accessor.getMethod().getAnnotations()).
					filter(annotation -> annotation.annotationType().equals(VirtualField.class)
					).count() == 0;
			return notObjectMethod && notVirtualField && !accessor.getMethod().isSynthetic();
        }).
		forEach((accessorByMethodNameEntry) -> {
            Collection<FieldAccessor> accessors = accessorByMethodNameEntry.getValue();

            FieldAccessor accessor = accessors.iterator().next();
            String fieldName = extractFieldName(accessor);
			Object mapValue = map.get(fieldName);
			if (mapValue == null) {
				throw new NullPointerException("Can't find field: " + fieldName + " in: " + map);
			}
			callback.apply(accessor, mapValue);
        });
	}

	private String extractFieldName(FieldAccessor accessor) {
		Optional<Mapping> fieldNameOptional = reflections.annotation(accessor.getMethod().getAnnotations(), Mapping.class);
		if (fieldNameOptional.isPresent()) return fieldNameOptional.get().mapFieldName();
		return beanNamingExpert.getFieldName(accessor.getMethodName(), "get");
	}

	private boolean isAGetter(FieldAccessor accessor) {
		return accessor.getType() == FieldAccessorType.GET;
	}
}
