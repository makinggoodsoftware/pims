package com.mgs.text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatternMatcher {
    public PatternMatchingResult match(String toMatch, String pattern) {
        List<MatchingCondition> matchingConditions = matchingConditions(pattern);

        Map<String, String> placeholders = new HashMap<>();
        String remainingToMatch = toMatch;
        MatchingCondition openCondition = null;
        for (MatchingCondition matchingCondition : matchingConditions) {
            if (matchingCondition.isLiteral()){
                String thisConditionPattern = matchingCondition.getPattern();
                if (openCondition != null){
                    int placeholderEndIndex = latestIndex (remainingToMatch, thisConditionPattern);
                    String placeholderValue = remainingToMatch.substring(0, placeholderEndIndex);
                    placeholders.put(openCondition.getPattern(), placeholderValue);
                    remainingToMatch = remainingToMatch.substring(placeholderEndIndex, remainingToMatch.length());
                    openCondition = null;
                }
                if (! remainingToMatch.startsWith(thisConditionPattern)) return new PatternMatchingResult(false, null);
                remainingToMatch = remainingToMatch.substring(thisConditionPattern.length(), remainingToMatch.length());
            } else {
                openCondition = matchingCondition;
            }
        }

        if (openCondition != null){
            if (remainingToMatch.length() < 0) throw new IllegalStateException();
            placeholders.put(openCondition.getPattern(), remainingToMatch);
        }

        return new PatternMatchingResult(true, placeholders);
    }

    private int latestIndex(String toMatch, String pattern) {
        int previousIndex = -1;
        while (true){
            int thisIndex = toMatch.indexOf(pattern, previousIndex + 1);
            if (thisIndex == -1 && previousIndex == -1) throw new IllegalStateException();
            if (thisIndex == -1) return previousIndex;
            previousIndex = thisIndex;
        }
    }

    private List<MatchingCondition> matchingConditions(String pattern) {
        List<MatchingCondition> matchingCoditions = new ArrayList<>();
        int nextPlaceholderIndex = pattern.indexOf("{");
        int currentPatternPoint = 0;
        int patternLastIndex = pattern.length() - 1;

        while (currentPatternPoint <= patternLastIndex){
            if (nextPlaceholderIndex == -1){
                matchingCoditions.add(new MatchingCondition(true, pattern.substring(currentPatternPoint, pattern.length())));
                currentPatternPoint = patternLastIndex + 1;
            }else if (nextPlaceholderIndex > currentPatternPoint) {
                String thisPatternPortion = pattern.substring(currentPatternPoint, nextPlaceholderIndex);
                matchingCoditions.add(new MatchingCondition(true, thisPatternPortion));
                currentPatternPoint = nextPlaceholderIndex;
            } else if (nextPlaceholderIndex == currentPatternPoint){
                int nextEndingPlaceholderIndex = nextEndingPlaceholderIndex(pattern, nextPlaceholderIndex);
                String placeHolderName = pattern.substring(nextPlaceholderIndex + 1, nextEndingPlaceholderIndex);
                matchingCoditions.add(new MatchingCondition(false, placeHolderName));
                currentPatternPoint = nextEndingPlaceholderIndex + 1;
                nextPlaceholderIndex = pattern.indexOf("{", nextPlaceholderIndex + 1);
            } else {
                throw new IllegalStateException();
            }
        }
        return matchingCoditions;
    }

    private int nextEndingPlaceholderIndex(String pattern, int nextPlaceholderIndex) {
        int nextEndingPlaceholderIndex = pattern.indexOf("}",nextPlaceholderIndex + 1);
        if (nextEndingPlaceholderIndex <= nextPlaceholderIndex) {
            throw new IllegalStateException();
        }
        return nextEndingPlaceholderIndex;
    }

    private class MatchingCondition {
        private final boolean literal;
        private final String pattern;

        public MatchingCondition(boolean literal, String pattern) {
            this.literal = literal;
            this.pattern = pattern;
        }

        public boolean isLiteral() {
            return literal;
        }

        public String getPattern() {
            return pattern;
        }
    }
}
