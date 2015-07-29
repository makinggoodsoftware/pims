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
        result.isMatch() == expectedMatch
        result.placeholders == expectedPlaceholders

        where:
        toMatch         | pattern             | expectedPlaceholders        |  expectedMatch
        "getSomething"  | "get{fieldName}"    | [fieldName:'something']     |  true
        "badName"       | "get{fieldName}"    | null                        |  false
        "literal"       | "literal"           | [:]                         |  true
        "getBlahById"   | "get{fieldName}ById"| [fieldName:'blah']          |  true
        "getByByById"   | "get{fieldName}ById"| [fieldName:'byBy']          |  true
        "getByByById"   | "get{a}By{b}"       | [a:'byBy', b:'id']          |  true
    }
}
