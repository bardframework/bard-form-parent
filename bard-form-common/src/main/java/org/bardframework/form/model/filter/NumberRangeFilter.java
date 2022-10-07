package org.bardframework.form.model.filter;

/**
 * Filter class for {@link Number} type attributes.
 *
 * @see RangeFilter
 */
public abstract class NumberRangeFilter<T extends Number & Comparable<T>, F extends NumberRangeFilter<T, F>> extends RangeFilter<T, F> {

}
