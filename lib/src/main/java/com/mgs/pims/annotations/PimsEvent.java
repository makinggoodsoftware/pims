package com.mgs.pims.annotations;

import com.mgs.pims.core.PimsEventType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface PimsEvent {
	PimsEventType type();
}
