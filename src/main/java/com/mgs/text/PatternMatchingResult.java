package com.mgs.text;

import java.util.Map;

public class PatternMatchingResult {
    private final boolean match;
    private final Map<String, String> placeholders;

    public PatternMatchingResult(boolean match, Map<String, String> placeholders) {
        this.match = match;
        this.placeholders = placeholders;
    }

    public boolean isMatch() {
        return match;
    }

    public Map<String, String> getPlaceholders() {
        return placeholders;
    }
}
