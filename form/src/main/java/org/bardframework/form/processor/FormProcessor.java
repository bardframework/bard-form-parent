package org.bardframework.form.processor;

import org.bardframework.form.FormTemplate;
import org.bardframework.form.flow.FlowData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;

public interface FormProcessor extends Comparable<FormProcessor> {

    void process(String flowToken, Map<String, String> flowData, Map<String, String> formData, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception;

    boolean mustExecute(FlowData flowData);

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
