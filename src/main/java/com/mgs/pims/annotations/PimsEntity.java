package com.mgs.pims.annotations;

import com.mgs.pims.core.NullMixer;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(value = RUNTIME)
public @interface PimsEntity {
    Class managedBy() default NullMixer.class;
}
