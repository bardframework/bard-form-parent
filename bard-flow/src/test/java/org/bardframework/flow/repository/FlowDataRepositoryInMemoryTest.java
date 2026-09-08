package org.bardframework.flow.repository;

import org.bardframework.flow.FlowData;
import org.bardframework.flow.exception.InvalidateFlowException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FlowDataRepositoryInMemoryTest {

    private static FlowData flowData(String key, Object value) {
        FlowData data = new FlowData();
        data.getData().put(key, value);
        return data;
    }

    @Test
    void put_thenGet_returnsTheStoredConversation() throws InvalidateFlowException {
        FlowDataRepositoryInMemory<FlowData> repository = new FlowDataRepositoryInMemory<>(60_000);
        FlowData data = flowData("mobile", "09120000000");

        repository.put("token-1", data);

        assertThat(repository.get("token-1")).isSameAs(data);
        assertThat(repository.get("token-1").getData()).containsEntry("mobile", "09120000000");
    }

    @Test
    void contains_reflectsPresence() {
        FlowDataRepositoryInMemory<FlowData> repository = new FlowDataRepositoryInMemory<>(60_000);

        assertThat(repository.contains("token-1")).isFalse();
        repository.put("token-1", new FlowData());
        assertThat(repository.contains("token-1")).isTrue();
    }

    @Test
    @DisplayName("an unknown token is rejected, not answered with null")
    void get_throwsForUnknownToken() {
        FlowDataRepositoryInMemory<FlowData> repository = new FlowDataRepositoryInMemory<>(60_000);

        assertThatThrownBy(() -> repository.get("unknown"))
                .isInstanceOf(InvalidateFlowException.class);
    }

    @Test
    @DisplayName("an expired conversation is indistinguishable from a forged token")
    void get_throwsAfterExpiration() {
        FlowDataRepositoryInMemory<FlowData> repository = new FlowDataRepositoryInMemory<>(0);
        repository.put("token-1", new FlowData());

        assertThatThrownBy(() -> repository.get("token-1"))
                .isInstanceOf(InvalidateFlowException.class);
    }

    @Test
    void remove_evictsTheConversation() {
        FlowDataRepositoryInMemory<FlowData> repository = new FlowDataRepositoryInMemory<>(60_000);
        repository.put("token-1", new FlowData());

        repository.remove("token-1");

        assertThat(repository.contains("token-1")).isFalse();
    }

    @Test
    void remove_isSilentForUnknownToken() {
        new FlowDataRepositoryInMemory<FlowData>(60_000).remove("unknown");
    }

    @Test
    void put_overwritesPreviousStateForTheSameToken() throws InvalidateFlowException {
        FlowDataRepositoryInMemory<FlowData> repository = new FlowDataRepositoryInMemory<>(60_000);
        repository.put("token-1", flowData("step", 1));
        repository.put("token-1", flowData("step", 2));

        assertThat(repository.get("token-1").getData()).containsEntry("step", 2);
    }

    @Test
    void conversationsAreIsolatedByToken() throws InvalidateFlowException {
        FlowDataRepositoryInMemory<FlowData> repository = new FlowDataRepositoryInMemory<>(60_000);
        repository.put("a", flowData("owner", "a"));
        repository.put("b", flowData("owner", "b"));

        assertThat(repository.get("a").getData()).containsEntry("owner", "a");
        assertThat(repository.get("b").getData()).containsEntry("owner", "b");
    }

    @Test
    @DisplayName("locale survives a store/restore round trip")
    void localeIsCarriedInFlowData() throws InvalidateFlowException {
        FlowDataRepositoryInMemory<FlowData> repository = new FlowDataRepositoryInMemory<>(60_000);
        FlowData data = new FlowData();
        data.setLocale(Locale.forLanguageTag("fa"));

        repository.put("token-1", data);

        assertThat(repository.get("token-1").getLocale()).isEqualTo(Locale.forLanguageTag("fa"));
    }
}
