package com.mgs.pims.annotations;

import com.mgs.pims.core.PimsMethodParameterType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface PimsParameter {
	PimsMethodParameterType type();

	String name() default "";
}
