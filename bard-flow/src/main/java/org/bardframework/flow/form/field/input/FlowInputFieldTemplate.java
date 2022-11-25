package org.bardframework.flow.form.field.input;

import jakarta.servlet.http.HttpServletResponse;
import org.bardframework.flow.form.FormProcessor;
import org.bardframework.form.field.input.InputField;
import org.bardframework.form.field.input.InputFieldTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public abstract class FlowInputFieldTemplate<F extends InputField<T>, T> extends InputFieldTemplate<F, T> {

    protected Map<String, List<FormProcessor>> actionProcessors = new HashMap<>();

    protected FlowInputFieldTemplate(String name) {
        super(name);
    }

    public FlowInputFieldTemplate(String name, boolean persistentValue) {
        super(name, persistentValue);
    }

    public Map<String, List<FormProcessor>> getActionProcessors() {
        return actionProcessors;
    }

    public void setActionProcessors(Map<String, List<FormProcessor>> actionProcessors) {
        this.actionProcessors = actionProcessors;
    }

    public void preProcess(String flowToken, Map<String, String> flowData, Locale locale, HttpServletResponse httpResponse) throws Exception {

    }
}
