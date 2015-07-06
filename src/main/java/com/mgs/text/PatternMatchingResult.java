package com.mgs.text;

public class PatternMatchingResult {
    private final boolean match;

    public PatternMatchingResult(boolean match) {
        this.match = match;
    }

    public boolean isMatch() {
        return match;
    }
}
