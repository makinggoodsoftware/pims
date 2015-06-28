package com.pims.core;

import com.reflections.FieldAccessor;

@FunctionalInterface
public interface OnMapFieldCallback {
	void apply(FieldAccessor fieldAccessor, Object mapValue);
}
