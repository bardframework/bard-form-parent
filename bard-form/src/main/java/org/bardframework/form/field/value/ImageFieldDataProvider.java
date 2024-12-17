package org.bardframework.form.field.value;

import org.bardframework.form.field.view.ImageViewField;

import java.util.Map;

public interface ImageFieldDataProvider {

    void fillData(ImageViewField field, Map<String, Object> args) throws Exception;
}
