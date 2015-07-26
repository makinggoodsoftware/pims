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
        "getSomething"  | "get{fieldName}"    | [fieldName:'Something']     |  true
        "badName"       | "get{fieldName}"    | null                        |  false
        "literal"       | "literal"           | [:]                         |  true
        "getBlahById"   | "get{fieldName}ById"| [fieldName:'Blah']          |  true
        "getByByById"   | "get{fieldName}ById"| [fieldName:'ByBy']          |  true
        "getByByById"   | "get{a}By{b}"       | [a:'ByBy', b:'Id']          |  true
    }
}
