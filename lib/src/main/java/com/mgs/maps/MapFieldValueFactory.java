package com.mgs.maps;

import com.mgs.reflections.*;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class MapFieldValueFactory {
	private final Reflections reflections;
	private final TypeParser typeParser;

	public MapFieldValueFactory(Reflections reflections, TypeParser typeParser) {
		this.reflections = reflections;
		this.typeParser = typeParser;
	}

	public Object transform(FieldAccessor fieldAccessor, ParsedType type, Object value, Class specialCase, OnMapFieldCallback onSpecialCaseProcessor) {
		assertParsedTypeIsResolved(type);
		assertNoOptionalFieldIsSet(type, value);

		Class<?> declaredType = type.getActualType().get();
		if (reflections.isSimple(declaredType)) return value;
		if (reflections.isAssignableTo(declaredType, specialCase)) {
			return onSpecialCaseProcessor.apply(fieldAccessor, value);
		}
		if (reflections.isCollection(declaredType)) {
			List listOfValues = (List) value;
			Declaration typeOfCollection = type.getOwnDeclaration().getParameters().values().iterator().next();
			//noinspection unchecked
			return listOfValues.stream().
				map((old) ->
                        transform(fieldAccessor, typeParser.parse(typeOfCollection), old, specialCase, onSpecialCaseProcessor)
                ).collect(
					toList()
				);
		}
		if (reflections.isAssignableTo(declaredType, Optional.class)) {
			if (value == null) return Optional.empty();
			Declaration typeOfOptional = type.getOwnDeclaration().getParameters().values().iterator().next();
			return Optional.of(transform(fieldAccessor, typeParser.parse(typeOfOptional), value, specialCase, onSpecialCaseProcessor));
		}
		throw new IllegalStateException("Invalid data in the map: " + value);
	}

	private void assertParsedTypeIsResolved(ParsedType returnType) {
		if (! returnType.getActualType().isPresent()) {
			throw new IllegalStateException("Can't map into a type which is not resolved");
		}
	}

	private void assertNoOptionalFieldIsSet(ParsedType returnType, Object rawValue) {
		if (!isOptional(returnType)) {
			if (rawValue == null) throw new IllegalStateException("Receiving null for a field that is not Optional" + returnType);
		}
	}

	private boolean isOptional(ParsedType parsedType) {
		Optional<Class> actualType = parsedType.getActualType();
		return actualType.isPresent() && actualType.get().equals(Optional.class);
	}
}
