package org.bardframework.table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Getter
@Setter
@ToString
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
}
