package org.bardframework.form.table;

import org.bardframework.form.FormTemplate;
import org.bardframework.form.common.table.TableModel;
import org.bardframework.form.table.header.TableHeaderTemplate;
import org.springframework.context.MessageSource;

import java.util.List;

public class TableTemplate extends TableModel {

    private final MessageSource messageSource;

    private final List<TableHeaderTemplate<?, ?>> headerTemplates;
    private FormTemplate filterFormTemplate;
    private FormTemplate saveFormTemplate;
    private FormTemplate updateFormTemplate;

    public TableTemplate(List<TableHeaderTemplate<?, ?>> headerTemplates, MessageSource messageSource) {
        this.headerTemplates = headerTemplates;
        this.messageSource = messageSource;
    }

    public List<TableHeaderTemplate<?, ?>> getHeaderTemplates() {
        return headerTemplates;
    }

    public FormTemplate getFilterFormTemplate() {
        return filterFormTemplate;
    }

    public void setFilterFormTemplate(FormTemplate filterFormTemplate) {
        this.filterFormTemplate = filterFormTemplate;
    }

    public FormTemplate getSaveFormTemplate() {
        return saveFormTemplate;
    }

    public void setSaveFormTemplate(FormTemplate saveFormTemplate) {
        this.saveFormTemplate = saveFormTemplate;
    }

    public FormTemplate getUpdateFormTemplate() {
        return updateFormTemplate;
    }

    public void setUpdateFormTemplate(FormTemplate updateFormTemplate) {
        this.updateFormTemplate = updateFormTemplate;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }
}
