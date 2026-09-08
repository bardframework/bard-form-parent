package org.bardframework.form.model.filter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * معناشناسی {@code isEmpty} حیاتی است: لایه‌ی دیتابیس بر اساس آن تصمیم می‌گیرد فیلتر «بدون محدودیت» را
 * خطا اعلام کند یا نه، و یک اشتباه در آن به حذف ناخواسته‌ی داده منجر می‌شود.
 */
class FilterTest {

    @Nested
    class BaseOperations {

        @Test
        void freshFilterIsEmpty() {
            assertThat(new LongFilter().isEmpty()).isTrue();
        }

        @Test
        void equalsMakesItNonEmpty() {
            assertThat(new LongFilter().setEquals(1L).isEmpty()).isFalse();
        }

        @Test
        void notEqualsMakesItNonEmpty() {
            assertThat(new LongFilter().setNotEquals(1L).isEmpty()).isFalse();
        }

        @Test
        void specifiedMakesItNonEmpty() {
            assertThat(new LongFilter().setSpecified(Boolean.TRUE).isEmpty()).isFalse();
            assertThat(new LongFilter().setSpecified(Boolean.FALSE).isEmpty()).isFalse();
        }

        @Test
        void inMakesItNonEmpty() {
            assertThat(new LongFilter().setIn(List.of(1L, 2L)).isEmpty()).isFalse();
        }

        @Test
        @DisplayName("an empty 'in' collection does not count as a restriction")
        void emptyInCollectionIsStillEmpty() {
            assertThat(new LongFilter().setIn(List.of()).isEmpty()).isTrue();
            assertThat(new LongFilter().setNotIn(List.of()).isEmpty()).isTrue();
        }

        @Test
        void settersReturnTheFilterForChaining() {
            LongFilter filter = new LongFilter().setEquals(1L).setSpecified(true);
            assertThat(filter.getEquals()).isEqualTo(1L);
            assertThat(filter.getSpecified()).isTrue();
        }
    }

    @Nested
    class Range {

        @Test
        void fromMakesItNonEmpty() {
            assertThat(new LongFilter().setFrom(1L).isEmpty()).isFalse();
        }

        @Test
        void toMakesItNonEmpty() {
            assertThat(new LongFilter().setTo(10L).isEmpty()).isFalse();
        }

        @Test
        void rangeFilterKeepsBaseSemantics() {
            assertThat(new LongFilter().setIn(List.of(1L)).isEmpty()).isFalse();
        }
    }

    @Nested
    class Text {

        @Test
        void freshStringFilterIsEmpty() {
            assertThat(new StringFilter().isEmpty()).isTrue();
        }

        @Test
        void containsMakesItNonEmpty() {
            assertThat(new StringFilter().setContains("a").isEmpty()).isFalse();
        }

        @Test
        void doesNotContainMakesItNonEmpty() {
            assertThat(new StringFilter().setDoesNotContain("a").isEmpty()).isFalse();
        }

        @Test
        void startWithMakesItNonEmpty() {
            assertThat(new StringFilter().setStartWith("a").isEmpty()).isFalse();
        }

        @Test
        void endWithMakesItNonEmpty() {
            assertThat(new StringFilter().setEndWith("a").isEmpty()).isFalse();
        }

        @Test
        @DisplayName("an empty string is not a restriction")
        void emptyStringIsStillEmpty() {
            assertThat(new StringFilter().setContains("").isEmpty()).isTrue();
        }

        @Test
        void inheritedRangeAndBaseOperationsStillApply() {
            assertThat(new StringFilter().setEquals("a").isEmpty()).isFalse();
            assertThat(new StringFilter().setFrom("a").isEmpty()).isFalse();
        }
    }

    @Nested
    class Identity {

        @Test
        void idFilterBehavesLikeOtherFilters() {
            assertThat(new IdFilter<String>().isEmpty()).isTrue();
            assertThat(new IdFilter<String>().setEquals("x").isEmpty()).isFalse();
            assertThat(new IdFilter<String>().setIn(List.of("x", "y")).isEmpty()).isFalse();
        }
    }

    @Nested
    class OtherTypes {

        @Test
        void allConcreteFiltersStartEmpty() {
            assertThat(new BooleanFilter().isEmpty()).isTrue();
            assertThat(new IntegerFilter().isEmpty()).isTrue();
            assertThat(new ShortFilter().isEmpty()).isTrue();
            assertThat(new ByteFilter().isEmpty()).isTrue();
            assertThat(new DoubleFilter().isEmpty()).isTrue();
            assertThat(new FloatFilter().isEmpty()).isTrue();
            assertThat(new BigDecimalFilter().isEmpty()).isTrue();
            assertThat(new BigIntegerFilter().isEmpty()).isTrue();
            assertThat(new InstantFilter().isEmpty()).isTrue();
            assertThat(new LocalTimeFilter().isEmpty()).isTrue();
            assertThat(new DurationFilter().isEmpty()).isTrue();
        }
    }
}
