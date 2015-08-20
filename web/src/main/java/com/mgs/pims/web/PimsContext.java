package com.mgs.pims.web;

import com.mgs.pims.types.map.PimsMapEntity;
import com.mgs.pims.types.provider.PimsProvider;
import com.mgs.pims.types.retriever.PimsRetriever;

import java.util.Map;

public class PimsContext {
    private final Map<String, PimsRetriever> retrievers;
    private final Map<String, PimsProvider> providers;

    public PimsContext(Map<String, PimsRetriever> retrievers, Map<String, PimsProvider> providers) {
        this.retrievers = retrievers;
        this.providers = providers;
    }

    public PimsRetriever retriever(String name) {
        return retrievers.get(name);
    }

    public <T extends PimsMapEntity> PimsProvider<T> provider(String name) {
        return providers.get(name);
    }
}
