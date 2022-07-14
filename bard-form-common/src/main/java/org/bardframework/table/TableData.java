package org.bardframework.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TableData {
    private List<String> headers;
    private List<Map<String, List<Object>>> data;
    private long total;

    public void addData(String id, List<Object> row) {
        if (null == this.data) {
            this.data = new ArrayList<>();
        }
        this.data.add(Map.of(id, row));
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<Map<String, List<Object>>> getData() {
        return data;
    }

    public void setData(List<Map<String, List<Object>>> data) {
        this.data = data;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
