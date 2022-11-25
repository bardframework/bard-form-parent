package org.bardframework.form.field.value;

import jakarta.servlet.http.HttpServletRequest;
import org.bardframework.form.field.view.FileDownloadField;

import java.util.Map;

public interface FileFieldDataProvider {

    void fillData(FileDownloadField field, Map<String, String> arg, HttpServletRequest httpRequest) throws Exception;
}
