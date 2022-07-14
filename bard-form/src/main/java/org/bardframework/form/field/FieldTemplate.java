package org.bardframework.form.field;

import org.bardframework.commons.utils.ReflectionUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;

public abstract class FieldTemplate<F extends Field> {
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    protected final Class<F> fieldClazz;
    protected Expression showExpression = null;
    protected final String name;

    protected FieldTemplate(String name) {
        this.name = name;
        this.fieldClazz = ReflectionUtils.getGenericArgType(this.getClass(), 0);
    }

    public F toField(FormTemplate formTemplate, Map<String, String> args, Locale locale, HttpServletRequest httpRequest) throws Exception {
        F field = this.getEmptyField();
        this.fill(formTemplate, field, args, locale, httpRequest);
        return field;
    }

    protected void fill(FormTemplate formTemplate, F field, Map<String, String> args, Locale locale, HttpServletRequest httpRequest) throws Exception {
        field.setTitle(FormUtils.getFieldStringProperty(formTemplate, this, "title", locale, args, null));
        field.setDescription(FormUtils.getFieldStringProperty(formTemplate, this, "description", locale, args, null));
    }

    protected F getEmptyField() {
        return ReflectionUtils.newInstance(this.fieldClazz);
    }

    public String getName() {
        return name;
    }

    public void setShowExpression(String showExpression) {
        this.showExpression = new SpelExpressionParser(new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE, null)).parseExpression(showExpression);
    }

    public boolean mustShow(Map<String, String> args) {
        return null == showExpression || Boolean.TRUE.equals(showExpression.getValue(new StandardEvaluationContext(args), Boolean.class));
    }
}
