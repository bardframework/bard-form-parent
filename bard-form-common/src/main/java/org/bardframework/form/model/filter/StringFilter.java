package org.bardframework.form.model.filter;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * Class for filtering attributes with {@link String} type.
 * It can be added to a criteria class as a member, to support the following query parameters:
 * <code>
 * fieldName.equals='something'
 * fieldName.specified=true
 * fieldName.specified=false
 * fieldName.in='something','other'
 * fieldName.notIn='something','other'
 * fieldName.contains='thing'
 * </code>
 */
@Slf4j
@Getter
@ToString
public class StringFilter extends Filter<String, StringFilter> {

    private String contains;
    private String doesNotContain;
    private String startWith;
    private String endWith;

    /**
     * <p>Setter for the field <code>doesNotContain</code>.</p>
     *
     * @param doesNotContain a {@link String} object.
     * @return a {@link StringFilter} object.
     */
    public StringFilter setDoesNotContain(String doesNotContain) {
        this.doesNotContain = doesNotContain;
        return this;
    }

    /**
     * <p>Setter for the field <code>contains</code>.</p>
     *
     * @param contains a {@link String} object.
     * @return a {@link StringFilter} object.
     */
    public StringFilter setContains(String contains) {
        this.contains = contains;
        return this;
    }

    public StringFilter setStartWith(String startWith) {
        this.startWith = startWith;
        return this;
    }

    public StringFilter setEndWith(String endWith) {
        this.endWith = endWith;
        return this;
    }
}
