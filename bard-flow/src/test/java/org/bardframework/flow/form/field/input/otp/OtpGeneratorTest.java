package org.bardframework.flow.form.field.input.otp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class OtpGeneratorTest {

    @Test
    @DisplayName("a regex generator produces codes matching its pattern")
    void regexGenerator_producesMatchingCodes() {
        OtpGeneratorRegex generator = new OtpGeneratorRegex("[0-9]{6}");

        for (int i = 0; i < 50; i++) {
            assertThat(generator.generate()).matches("[0-9]{6}");
        }
    }

    @Test
    void regexGenerator_reportsLengthAndNumericFlag() {
        OtpGeneratorRegex generator = new OtpGeneratorRegex("[0-9]{6}");

        assertThat(generator.getLength()).isEqualTo(6);
        assertThat(generator.isNumber()).isTrue();
    }

    @Test
    void regexGenerator_detectsNonNumericPatterns() {
        OtpGeneratorRegex generator = new OtpGeneratorRegex("[A-Z]{5}");

        assertThat(generator.getLength()).isEqualTo(5);
        assertThat(generator.isNumber()).isFalse();
        assertThat(generator.generate()).matches("[A-Z]{5}");
    }

    @Test
    @DisplayName("successive codes differ — a predictable OTP is not an OTP")
    void regexGenerator_isNotConstant() {
        OtpGeneratorRegex generator = new OtpGeneratorRegex("[0-9]{6}");
        Set<String> codes = new HashSet<>();
        for (int i = 0; i < 50; i++) {
            codes.add(generator.generate());
        }
        assertThat(codes).hasSizeGreaterThan(1);
    }

    @Test
    @DisplayName("the fixed generator always returns the configured code — tests only")
    void fixedGenerator_alwaysReturnsTheSameCode() {
        OtpGeneratorFixed generator = new OtpGeneratorFixed("123456");

        assertThat(generator.generate()).isEqualTo("123456");
        assertThat(generator.generate()).isEqualTo("123456");
        assertThat(generator.getLength()).isEqualTo(6);
    }
}
