package org.bardframework.form.model.filter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.ToString;

import java.util.Collection;
import java.util.List;

/**
 * Base class for the various attribute filters. It can be added to a criteria class as a member, to support the
 * following query parameters:
 * <pre>
 *      fieldName.equals='something'
 *      fieldName.specified=true
 *      fieldName.specified=false
 *      fieldName.notEquals='somethingElse'
 *      fieldName.in='something','other'
 *      fieldName.notIn='something','other'
 * </pre>
 */
@Getter
@ToString
public abstract class Filter<T, F> {
    private T equals;
    private T notEquals;
    private Boolean specified;
    private Collection<T> in;
    private Collection<T> notIn;

    /**
     * <p>Setter for the field <code>equals</code>.</p>
     *
     * @param equals a T object.
     * @return a {@link Filter} object.
     */
    public F setEquals(T equals) {
        this.equals = equals;
        return (F) this;
    }

    /**
     * <p>Setter for the field <code>notEquals</code>.</p>
     *
     * @param notEquals a T object.
     * @return a {@link Filter} object.
     */
    public F setNotEquals(T notEquals) {
        this.notEquals = notEquals;
        return (F) this;
    }

    /**
     * <p>Setter for the field <code>specified</code>.</p>
     *
     * @param specified a {@link Boolean} object.
     * @return a {@link Filter} object.
     */
    public F setSpecified(Boolean specified) {
        this.specified = specified;
        return (F) this;
    }

    /**
     * <p>Setter for the field <code>in</code>.</p>
     *
     * @param in a {@link Collection} object.
     * @return a {@link Filter} object.
     */
    public F setIn(Collection<T> in) {
        this.in = in;
        return (F) this;
    }


    /**
     * <p>Setter for the field <code>in</code>.</p>
     *
     * @param in a {@link Collection} object.
     * @return a {@link Filter} object.
     */
    @JsonIgnore
    public F setIn(T... in) {
        return this.setIn(List.of(in));
    }

    /**
     * <p>Setter for the field <code>notIn</code>.</p>
     *
     * @param notIn a {@link Collection} object.
     * @return a {@link Filter} object.
     */
    public F setNotIn(Collection<T> notIn) {
        this.notIn = notIn;
        return (F) this;
    }

    /**
     * <p>Setter for the field <code>notIn</code>.</p>
     *
     * @param notIn a {@link Collection} object.
     * @return a {@link Filter} object.
     */
    @JsonIgnore
    public F setNotIn(T... notIn) {
        return this.setNotIn(List.of(notIn));
    }

    public boolean isEmpty() {
        return null == this.getEquals()
                && null == this.getNotEquals()
                && null == this.getSpecified()
                && (null == this.getIn() || this.getIn().isEmpty())
                && (null == this.getNotIn() || this.getNotIn().isEmpty());
    }
}
