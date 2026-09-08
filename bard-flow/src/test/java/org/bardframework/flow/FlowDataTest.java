package org.bardframework.flow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

class FlowDataTest {

    @Test
    void newFlowDataHasEmptyDataAndZeroIndex() {
        FlowData data = new FlowData();

        assertThat(data.getData()).isEmpty();
        assertThat(data.getCurrentFormIndex()).isZero();
    }

    @Test
    @DisplayName("the locale is stored inside the conversation data under a reserved key")
    void setLocale_storesLanguageTagInData() {
        FlowData data = new FlowData();

        data.setLocale(Locale.forLanguageTag("fa"));

        assertThat(data.getData()).containsEntry(FlowData.LANGUAGE_FIELD_NAME, "fa");
        assertThat(data.getLocale()).isEqualTo(Locale.forLanguageTag("fa"));
    }

    @Test
    void getLocale_returnsNullWhenNotSet() {
        assertThat(new FlowData().getLocale()).isNull();
    }

    @Test
    void setLocale_returnsItselfForChaining() {
        FlowData data = new FlowData();
        assertThat(data.setLocale(Locale.ENGLISH)).isSameAs(data);
    }

    @Test
    void currentFormIndexIsMutable() {
        FlowData data = new FlowData();
        data.setCurrentFormIndex(3);
        assertThat(data.getCurrentFormIndex()).isEqualTo(3);
    }
}
