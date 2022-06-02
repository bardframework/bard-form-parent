package org.bardframework.form.common.table;

import java.util.ArrayList;
import java.util.List;

public class TableData {
    private List<String> headers;
    private List<List<Object>> data;
    private long total;

    public void addData(List<Object> row) {
        if (null == this.data) {
            this.data = new ArrayList<>();
        }
        this.data.add(row);
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<List<Object>> getData() {
        return data;
    }

    public void setData(List<List<Object>> data) {
        this.data = data;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
