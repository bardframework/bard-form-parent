package org.bardframework.flow.repository;

import org.bardframework.flow.exception.InvalidateFlowException;

public interface FlowDataRepository<D> {

    boolean contains(String token);

    void put(String token, D data);

    /**
     * @return data associated with given token, null otherwise
     */
    D get(String token) throws InvalidateFlowException;

    void remove(String token);
}
