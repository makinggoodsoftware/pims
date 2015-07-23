package com.mgs.pims.core;

public class PimsMethodCallParameters {
    private final Object proxy;
    private final Object[] args;

    public PimsMethodCallParameters(Object proxy, Object[] args) {
        this.proxy = proxy;
        this.args = args;
    }
}
