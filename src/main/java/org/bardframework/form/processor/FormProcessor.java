package org.bardframework.form.processor;

import org.bardframework.form.template.FormTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;

public interface FormProcessor {
    void process(String flowToken, Map<String, String> flowData, Map<String, String> formData, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception;

    default void configurationValidate(FormTemplate formTemplate) {

    }
}
