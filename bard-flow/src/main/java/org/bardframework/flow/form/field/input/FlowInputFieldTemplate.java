package org.bardframework.flow.form.field.input;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.bardframework.flow.form.FormProcessor;
import org.bardframework.form.field.input.InputField;
import org.bardframework.form.field.input.InputFieldTemplateAbstract;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Getter
@Setter
public abstract class FlowInputFieldTemplate<F extends InputField<T>, T> extends InputFieldTemplateAbstract<F, T> {

    protected Map<String, List<FormProcessor>> actionProcessors = new HashMap<>();

    protected FlowInputFieldTemplate(String name) {
        super(name);
    }

    public FlowInputFieldTemplate(String name, boolean persistentValue) {
        super(name, persistentValue);
    }

    public void preProcess(String flowToken, Map<String, String> flowData, Locale locale, HttpServletResponse httpResponse) throws Exception {

    }
}
