package edu.oregonstate.mist

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(User)
class UserSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "initialize user"() {
        given:
            final String NAME = "name"
            final String EMAIL = "email@address.com"
            final String PASSWORD = "pass123word"

        when:
            User testUser = new User(NAME, EMAIL, PASSWORD)
        then:
            testUser.getName() == NAME
            testUser.getEmail() == EMAIL
            testUser.passwordEquals(PASSWORD)
    }

    void "mutate user"() {
        given:
            final String NAME = "name"
            final String EMAIL = "email@address.com"
            final String NEW_EMAIL = "address@email.com"
            final String PASSWORD = "pass123word"
            final String NEW_PASSWORD = "word123pass"
            User testUser = new User(NAME, EMAIL, PASSWORD)

        when:
            testUser.setEmail(NEW_EMAIL)
        then:
            testUser.getEmail() == NEW_EMAIL

        when:
            testUser.setPassword(NEW_PASSWORD)
        then:
            testUser.passwordEquals(NEW_PASSWORD)
    }

    void "calculate sha256sum string from string"() {
        expect:
        User.sha256sum(message) == digest

        where:
        message                                       | digest
        "hello world"                                 | "b94d27b9934d3e08a52e52d7da7dabfac484efe37a5380ee9088f7ace2efcde9"
        "the quick brown fox jumps over the lazy dog" | "05c6e08f1d9fdafa03147fcb8f82f124c76d2f70e3d989dc8aadb5e7d7450bec"
        "password"                                    | "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8"
    }

    void "validate password"() {
        given:
            User testUser = new User("name", "email", password)

        expect:
            testUser.validate(["passwordTemp"]) == result
            testUser.passwordHash == User.hash(password)
            testUser.passwordTemp == null

        where:
            password      | result
            "password"    | false  //  <10 characters,  <1 digit
            "mypassword"  | false  // ==10 characters,  <1 digit
            "ourpassword" | false  //  >10 characters,  <1 digit
            "password1"   | false  //  <10 characters, ==1 digit
            "apassword1"  | true   // ==10 characters, ==1 digit
            "mypassword1" | true   //  >10 characters, ==1 digit
            "pass23"      | false  //  <10 characters,  >1 digit
            "password12"  | true   // ==10 characters,  >1 digit
            "password123" | true   //  >10 characters,  >1 digit
    }
}
