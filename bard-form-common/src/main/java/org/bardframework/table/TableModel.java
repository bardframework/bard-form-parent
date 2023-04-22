package org.bardframework.table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.bardframework.form.BardForm;
import org.bardframework.table.header.TableHeader;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Setter
@ToString
public class TableModel {
    private String name;
    private String title;
    private String description;
    private Integer fetchSize;

    private List<TableHeader> headers;

    private BardForm filterForm;
    private BardForm saveForm;
    private BardForm updateForm;

    private Boolean delete;
    private Boolean print;
    private Boolean export;
    private Boolean preload;
    private Boolean pageable;
    private Boolean hideColumn;

    public void addHeader(TableHeader header) {
        if (null == this.headers) {
            this.headers = new ArrayList<>();
        }
        this.headers.add(header);
    }
}
