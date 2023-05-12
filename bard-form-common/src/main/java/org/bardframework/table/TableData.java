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
    private List<String> headers = new ArrayList<>();
    private List<Map<String, List<Object>>> data = new ArrayList<>();
    private long total;

    public void addData(String id, List<Object> row) {
        this.data.add(Map.of(id, row));
    }

    public void addHeader(String header) {
        this.headers.add(header);
    }
}
