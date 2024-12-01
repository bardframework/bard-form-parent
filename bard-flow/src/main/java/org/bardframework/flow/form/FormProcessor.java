package org.bardframework.flow.form;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bardframework.form.FormTemplate;

import java.util.Locale;
import java.util.Map;

public interface FormProcessor extends Comparable<FormProcessor> {

    void process(String flowToken, Map<String, Object> flowData, Map<String, Object> formData, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception;

    boolean mustExecute(Map<String, Object> args);

    /**
     * @return processor order, smaller number has higher priority.
     */
    default int order() {
        return 0;
    }

    default void configurationValidate(FormTemplate formTemplate) {

    }

    @Override
    default int compareTo(FormProcessor other) {
        return Integer.compare(this.order(), other.order());
    }
}
