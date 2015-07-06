package com.mgs.text

import spock.lang.Specification
import spock.lang.Unroll

class PatternMatcherSpecification extends Specification {
    PatternMatcher testObj = new PatternMatcher()

    @Unroll
    def "should match" (){
        when:
        PatternMatchingResult result = testObj.match(toMatch, pattern)

        then:
        result.isMatch() == expectedResult

        where:
        toMatch         | pattern             | expectedResult
        "getSomething"  | "get{fieldName}"    | true
        "badName"       | "get{fieldName}"    | false
        "literal"       | "literal"           | true
    }
}
