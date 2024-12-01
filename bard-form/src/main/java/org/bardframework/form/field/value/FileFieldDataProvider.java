package org.bardframework.form.field.value;

import org.bardframework.form.field.view.FileDownloadField;

import java.util.Map;

public interface FileFieldDataProvider {

    void fillData(FileDownloadField field, Map<String, Object> args) throws Exception;
}
