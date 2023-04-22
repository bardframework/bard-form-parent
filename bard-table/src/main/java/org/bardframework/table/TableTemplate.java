package org.bardframework.table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.bardframework.form.FormTemplate;
import org.bardframework.table.header.HeaderTemplate;
import org.springframework.context.MessageSource;

import java.util.List;

@Slf4j
@Getter
@Setter
@ToString
public class TableTemplate extends TableModel {

    private final MessageSource messageSource;

    private final List<HeaderTemplate<?, ?>> headerTemplates;
    private final Class<?> modelClass;
    private FormTemplate filterFormTemplate;
    private FormTemplate saveFormTemplate;
    private FormTemplate updateFormTemplate;

    public TableTemplate(List<HeaderTemplate<?, ?>> headerTemplates, Class<?> modelClass, MessageSource messageSource) {
        this.modelClass = modelClass;
        this.headerTemplates = headerTemplates;
        this.messageSource = messageSource;
    }
}
