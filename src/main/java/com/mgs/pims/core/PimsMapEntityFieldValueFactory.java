package com.mgs.pims.core;

import com.reflections.Declaration;
import com.reflections.ParsedType;
import com.reflections.Reflections;
import com.reflections.TypeParser;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class PimsMapEntityFieldValueFactory {
	private final Reflections reflections;
	private final TypeParser typeParser;

	public PimsMapEntityFieldValueFactory(Reflections reflections, TypeParser typeParser) {
		this.reflections = reflections;
		this.typeParser = typeParser;
	}

	public Object transform(ParsedType type, Object value, OnMapEntityProcessor onComplexFieldValue) {
		assertParsedTypeIsResolved(type);
		assertNoOptionalFieldIsSet(type, value);

		Class<?> declaredType = type.getActualType().get();
		if (reflections.isSimple(declaredType)) return value;
		if (reflections.isAssignableTo(declaredType, PimsMapEntity.class)) {
			return onComplexFieldValue.process(type, value);
		}
		if (reflections.isCollection(declaredType)) {
			List listOfValues = (List) value;
			Declaration typeOfCollection = type.getOwnDeclaration().getParameters().values().iterator().next();
			//noinspection unchecked
			return listOfValues.stream().
				map((old) ->
					transform(typeParser.parse(typeOfCollection), old, onComplexFieldValue)
				).collect(
					toList()
				);
		}
		if (reflections.isAssignableTo(declaredType, Optional.class)) {
			if (value == null) return Optional.empty();
			Declaration typeOfOptional = type.getOwnDeclaration().getParameters().values().iterator().next();
			return Optional.of(transform(typeParser.parse(typeOfOptional), value, onComplexFieldValue));
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
