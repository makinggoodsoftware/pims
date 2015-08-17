package com.mgs.pims.web;

import com.mgs.pims.types.retriever.PimsRetriever;

import java.util.Map;

public class PimsContext {
    private final Map<String, PimsRetriever> retrievers;

    public PimsContext(Map<String, PimsRetriever> retrievers) {
        this.retrievers = retrievers;
    }

    public PimsRetriever retriever(String name) {
        return retrievers.get(name);
    }
}
