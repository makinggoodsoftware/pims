package com.mgs.text;

public class PatternMatcher {
    public PatternMatchingResult match(String toMatch, String pattern) {
        String compiledPattern = compilePattern(pattern);
        boolean match = toMatch.startsWith(compiledPattern);
        return new PatternMatchingResult(match);
    }

    private String compilePattern(String pattern) {
        int parameterizedPatternPos = pattern.indexOf("{");
        if (parameterizedPatternPos < 0) return pattern;
        return pattern.substring(0, parameterizedPatternPos);
    }
}
