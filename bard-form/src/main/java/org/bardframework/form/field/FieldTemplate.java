package org.bardframework.form.field;

import lombok.Getter;
import lombok.Setter;
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

import java.util.Locale;
import java.util.Map;

@Getter
@Setter
public abstract class FieldTemplate<F extends Field> {

    protected final Class<F> fieldClazz;
    protected final String name;
    protected Expression showExpression = null;
    protected F defaultValue;
    protected Logger log = LoggerFactory.getLogger(this.getClass());

    public FieldTemplate(String name) {
        this.name = name;
        this.fieldClazz = ReflectionUtils.getGenericArgType(this.getClass(), 0);
        this.defaultValue = ReflectionUtils.newInstance(this.fieldClazz);
    }

    public F toField(FormTemplate formTemplate, Map<String, String> args, Locale locale) throws Exception {
        F field = this.getEmptyField();
        this.fill(formTemplate, field, args, locale);
        return field;
    }

    protected void fill(FormTemplate formTemplate, F field, Map<String, String> args, Locale locale) throws Exception {
        field.setName(this.getName());
        field.setTitle(FormUtils.getFieldStringProperty(formTemplate, this, "title", locale, args, this.getDefaultValue().getTitle()));
        field.setDescription(FormUtils.getFieldStringProperty(formTemplate, this, "description", locale, args, this.getDefaultValue().getDescription()));
        field.setInfo(FormUtils.getFieldStringProperty(formTemplate, this, "info", locale, args, this.getDefaultValue().getInfo()));
    }

    protected F getEmptyField() {
        return ReflectionUtils.newInstance(this.fieldClazz);
    }

    public void setShowExpression(String showExpression) {
        this.showExpression = new SpelExpressionParser(new SpelParserConfiguration(SpelCompilerMode.MIXED, null)).parseExpression(showExpression);
    }

    public boolean mustShow(Map<String, String> args) {
        return null == showExpression || Boolean.TRUE.equals(showExpression.getValue(new StandardEvaluationContext(args), Boolean.class));
    }
}
