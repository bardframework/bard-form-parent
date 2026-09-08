package org.bardframework.form;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.StaticMessageSource;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * آبشار کلیدهای پیام، رفتار محوری فریمورک است: عنوان‌ها و حتی رفتار فرم (اجباری بودن، غیرفعال بودن و ...)
 * از همین مسیر خوانده می‌شوند. شکستن ترتیب آبشار یعنی تغییر خاموش رفتار همه‌ی فرم‌ها.
 */
class FormUtilsKeyResolutionTest {

    private static final Locale LOCALE = Locale.ENGLISH;

    private static StaticMessageSource messageSource(Map<String, String> messages) {
        StaticMessageSource source = new StaticMessageSource();
        messages.forEach((code, message) -> source.addMessage(code, LOCALE, message));
        return source;
    }

    private static String field(StaticMessageSource source, String property, String defaultValue) {
        return FormUtils.getString("field", property, List.of("customer", "firstName"), LOCALE, Map.of(), defaultValue, source);
    }

    @Test
    @DisplayName("the most specific key wins: field.<form>.<field>.<property>")
    void mostSpecificKeyWins() {
        StaticMessageSource source = messageSource(Map.of(
                "field.customer.firstName.title", "specific",
                "field.firstName.title", "per-field",
                "field.title", "global"));

        assertThat(field(source, "title", "fallback")).isEqualTo("specific");
    }

    @Test
    @DisplayName("without a form-specific key, the per-field key applies to every form")
    void fallsBackToFieldOnlyKey() {
        StaticMessageSource source = messageSource(Map.of(
                "field.firstName.title", "per-field",
                "field.title", "global"));

        assertThat(field(source, "title", "fallback")).isEqualTo("per-field");
    }

    @Test
    @DisplayName("a form-wide key applies to all fields of that form")
    void fallsBackToFormOnlyKey() {
        StaticMessageSource source = messageSource(Map.of("field.customer.title", "per-form"));

        assertThat(field(source, "title", "fallback")).isEqualTo("per-form");
    }

    @Test
    @DisplayName("the bare key is the last resort before the template default")
    void fallsBackToBareKey() {
        StaticMessageSource source = messageSource(Map.of("field.title", "global"));

        assertThat(field(source, "title", "fallback")).isEqualTo("global");
    }

    @Test
    @DisplayName("with no key at all, the template's own default is used")
    void fallsBackToDefaultValue() {
        assertThat(field(messageSource(Map.of()), "title", "fallback")).isEqualTo("fallback");
    }

    @Test
    void returnsNullWhenNothingIsDefinedAndNoDefaultGiven() {
        assertThat(field(messageSource(Map.of()), "title", null)).isNull();
    }

    @Test
    @DisplayName("resolved values are template-filled from the argument map")
    void resolvedValueIsTemplateFilled() {
        StaticMessageSource source = messageSource(Map.of("field.customer.firstName.title", "Hello ::name::"));

        String value = FormUtils.getString("field", "title", List.of("customer", "firstName"),
                LOCALE, Map.of("name", "Ali"), null, source);

        assertThat(value).isEqualTo("Hello Ali");
    }

    @Test
    @DisplayName("the default value is template-filled too")
    void defaultValueIsTemplateFilled() {
        String value = FormUtils.getString("field", "title", List.of("customer", "firstName"),
                LOCALE, Map.of("name", "Ali"), "Hi ::name::", messageSource(Map.of()));

        assertThat(value).isEqualTo("Hi Ali");
    }

    @Test
    @DisplayName("form keys use the 'form' prefix and the form name only")
    void formPropertiesUseTheirOwnPrefix() {
        StaticMessageSource source = messageSource(Map.of("form.customer.submitLabel", "Save"));

        String value = FormUtils.getFormStringProperty("customer", "submitLabel", LOCALE, Map.of(), null, source);

        assertThat(value).isEqualTo("Save");
    }

    @Test
    @DisplayName("a blank message is treated as absent so the cascade continues")
    void blankMessageDoesNotStopTheCascade() {
        StaticMessageSource source = messageSource(Map.of(
                "field.customer.firstName.title", "   ",
                "field.firstName.title", "per-field"));

        assertThat(field(source, "title", "fallback")).isEqualTo("per-field");
    }

    @Test
    @DisplayName("behaviour, not only text, is resolved this way")
    void booleanPropertiesResolveThroughTheSameCascade() {
        StaticMessageSource source = messageSource(Map.of("field.firstName.required", "true"));

        assertThat(field(source, "required", "false")).isEqualTo("true");
    }
}
