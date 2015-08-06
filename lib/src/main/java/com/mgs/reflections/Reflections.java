package com.mgs.reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static java.util.Optional.empty;

public class Reflections {
	private final static List<Class> PRIMITIVE_WRAPPERS = Arrays.asList(
			String.class,
			Double.class,
			Integer.class,
			Float.class,
			Date.class
	);

	public boolean isSimple(Class<?> type) {
		return type.isPrimitive() || PRIMITIVE_WRAPPERS.contains(type);
	}

	public boolean isAssignableTo(Class<?> type, Class<?> assignableTo) {
		return assignableTo.isAssignableFrom(type);
	}

	public <T extends Annotation> Optional<T> annotation (Annotation[] annotations, Class<T> annotationType){
		for (Annotation annotation : annotations) {
			if (isAssignableTo(annotation.getClass(), annotationType))
				//noinspection unchecked
				return Optional.of((T) annotation);
		}
		return empty();
	}

	public boolean isCollection(Class<?> declaredType) {
		return isAssignableTo(declaredType, Collection.class);
	}

	public <T> SortedSet<T> walkInterfaceMethods(
			Class<?> from,
			UnaryOperator<Class> transformer,
			Comparator<T> comparator,
			Function<Method, Optional<T>> filter
	) {
		return walkInterfaceMethods(from, Optional.of(transformer), comparator, filter);
	}

	private <T> SortedSet<T> walkInterfaceMethods(
			Class<?> from,
			Optional<UnaryOperator<Class>> transformer,
			Comparator<T> comparator,
			Function<Method, Optional<T>> filter
	) {
		Class startingPoint = from;
		if (transformer.isPresent()) {
			startingPoint = transformer.get().apply(from);
		}
		SortedSet<T> methods = findMethods(startingPoint, filter, comparator);
		Class[] parentInterfaces = from.getInterfaces();
		for (Class parentInterface : parentInterfaces) {
			methods.addAll(walkInterfaceMethods(parentInterface, transformer, comparator, filter));
		}
		return methods;
	}

	private <T> SortedSet<T> findMethods(Class mixerType, Function<Method, Optional<T>> filter, Comparator<T> comparator) {
		SortedSet<T> possibleCandidates = new TreeSet<>(comparator);

		for (Method declaredMethod : mixerType.getDeclaredMethods()) {
			Optional<T> filterResult = filter.apply(declaredMethod);
			if (filterResult.isPresent()) {
				possibleCandidates.add(filterResult.get());
			}
		}
		return possibleCandidates;
	}

}
