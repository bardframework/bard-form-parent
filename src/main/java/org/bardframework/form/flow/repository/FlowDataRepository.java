package org.bardframework.form.flow.repository;

import org.bardframework.form.flow.exception.InvalidateFlowException;

public interface FlowDataRepository<D> {

    void put(String token, D data);

    /**
     * @return data associated with given token, null otherwise
     */
    D get(String token) throws InvalidateFlowException;

    void remove(String token);
}
