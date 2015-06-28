package com.reflections;

import java.util.Map;
import java.util.Optional;

public class ParsedType {
	private final Declaration ownDeclaration;
	private final Map<Class, ParsedType> superDeclarations;

	public ParsedType(Declaration ownDeclaration, Map<Class, ParsedType> superDeclarations) {
		this.ownDeclaration = ownDeclaration;
		this.superDeclarations = superDeclarations;
	}

	public Declaration getOwnDeclaration() {
		return ownDeclaration;
	}

	public Map<Class, ParsedType> getSuperDeclarations() {
		return superDeclarations;
	}

	public Optional<Class> getActualType() {
		return getOwnDeclaration().getTypeResolution().getSpecificClass();
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("ParsedType{");
		sb.append("ownDeclaration=").append(ownDeclaration);
		sb.append(", superDeclarations=").append(superDeclarations);
		sb.append('}');
		return sb.toString();
	}
}
