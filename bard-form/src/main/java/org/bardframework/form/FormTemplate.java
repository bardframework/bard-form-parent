package org.bardframework.form;

import org.apache.commons.collections4.MapUtils;
import org.bardframework.form.exception.FormDataValidationException;
import org.bardframework.form.field.FieldTemplate;
import org.bardframework.form.field.input.InputField;
import org.bardframework.form.field.input.InputFieldTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

public class FormTemplate extends Form {

    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    protected final MessageSource messageSource;
    protected final List<FieldTemplate<?>> fieldTemplates;
    protected Class<?> dtoClass;
    protected boolean failOnUnknownSubmitFields = true;
    protected Expression showExpression = null;

    public FormTemplate(String name, List<FieldTemplate<?>> fieldTemplates, MessageSource messageSource) {
        this.name = name;
        this.fieldTemplates = fieldTemplates;
        this.messageSource = messageSource;
    }

    public <F extends InputField<T>, T> void validate(Map<String, String> args, Map<String, String> values, Locale locale, HttpServletRequest httpRequest) throws Exception {
        Set<String> allowedFieldNames = this.getAllowedInputFields(args, locale);
        Set<String> illegalFields = new HashSet<>(values.keySet());
        illegalFields.removeAll(allowedFieldNames);
        if (!illegalFields.isEmpty()) {
            if (this.isFailOnUnknownSubmitFields()) {
                FormDataValidationException ex = new FormDataValidationException();
                illegalFields.forEach(illegalField -> ex.addFiledError(illegalField, "illegal field"));
                throw ex;
            } else {
                LOGGER.warn("illegal fields[{}] exist in form [{}].", illegalFields, this.getName());
            }
        }
        FormDataValidationException ex = new FormDataValidationException();
        for (InputFieldTemplate<?, ?> inputFieldTemplate : this.getInputFieldTemplates(args)) {
            InputFieldTemplate<F, T> template = (InputFieldTemplate<F, T>) inputFieldTemplate;
            F formField = template.toField(this, args, locale, httpRequest);
            if (Boolean.TRUE.equals(formField.getDisable())) {
                continue;
            }
            String stringValue = values.get(template.getName());
            if (!template.isValid(formField, template.toValue(stringValue))) {
                ex.addFiledError(template.getName(), formField.getErrorMessage());
            }
        }
        if (!MapUtils.isEmpty(ex.getInvalidFields())) {
            throw ex;
        }
    }

    public List<InputFieldTemplate<?, ?>> getInputFieldTemplates(Map<String, String> args) {
        List<InputFieldTemplate<?, ?>> inputFieldTemplates = new ArrayList<>();
        for (FieldTemplate<?> fieldTemplate : this.getFieldTemplates(args)) {
            if (!(fieldTemplate instanceof InputFieldTemplate<?, ?>)) {
                continue;
            }
            inputFieldTemplates.add((InputFieldTemplate<?, ?>) fieldTemplate);
        }
        return inputFieldTemplates;
    }

    /**
     * محاسبه لیست فیلدهایی که استفاده کننده مجاز به ارسال دیتا برای آن هاست
     * این فیلدها شامل تمامی اینپوت فیلدهایی است که فعال باشند
     */
    public Set<String> getAllowedInputFields(Map<String, String> args, Locale locale) throws Exception {
        List<InputFieldTemplate<?, ?>> inputFieldTemplates = this.getInputFieldTemplates(args);
        Set<String> allowedFieldNames = new HashSet<>();
        for (InputFieldTemplate<?, ?> inputFieldTemplate : inputFieldTemplates) {
            Boolean disable = FormUtils.getFieldBooleanProperty(this, inputFieldTemplate, "disable", locale, args, null);
            if (!Boolean.TRUE.equals(disable)) {
                allowedFieldNames.add(inputFieldTemplate.getName());
            }
        }
        return allowedFieldNames;
    }

    public void setShowExpression(String showExpression) {
        this.showExpression = new SpelExpressionParser(new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE, null)).parseExpression(showExpression);
    }

    public boolean mustShow(Map<String, String> args) {
        return null == showExpression || Boolean.TRUE.equals(showExpression.getValue(new StandardEvaluationContext(args), Boolean.class));
    }

    public List<FieldTemplate<?>> getFieldTemplates(Map<String, String> args) {
        return fieldTemplates.stream().filter(fieldTemplate -> fieldTemplate.mustShow(args)).collect(Collectors.toList());
    }

    public FieldTemplate<?> getField(String name, Map<String, String> args) {
        return this.getFieldTemplates(args).stream().filter(field -> field.getName().equals(name)).findFirst().orElse(null);
    }

    public Class<?> getDtoClass() {
        return dtoClass;
    }

    public void setDtoClass(Class<?> dtoClass) {
        this.dtoClass = dtoClass;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public boolean isFailOnUnknownSubmitFields() {
        return failOnUnknownSubmitFields;
    }

    public void setFailOnUnknownSubmitFields(boolean failOnUnknownSubmitFields) {
        this.failOnUnknownSubmitFields = failOnUnknownSubmitFields;
    }
}