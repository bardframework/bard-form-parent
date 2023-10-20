package org.bardframework.form;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.bardframework.commons.utils.ReflectionUtils;
import org.bardframework.form.exception.FormDataValidationException;
import org.bardframework.form.field.FieldTemplate;
import org.bardframework.form.field.input.InputFieldTemplate;
import org.springframework.context.MessageSource;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Setter
@ToString
public class FormTemplate {

    protected final String name;
    protected final MessageSource messageSource;
    protected final List<FieldTemplate<?>> fieldTemplates;
    protected FieldDescriptionShowType descriptionShowType = FieldDescriptionShowType.HINT;
    protected Class<?> dtoClass;
    protected boolean failOnUnknownSubmitFields = true;
    protected Boolean submitPristineInputs;
    protected Boolean submitEmptyInputs;
    protected Expression showExpression = null;
    protected List<FormTemplate> formTemplates = new ArrayList<>();
    protected NestedFormShowType nestedFormShowType = NestedFormShowType.MAIN_FORM;
    protected Integer autoSubmitDelaySeconds = null;

    public FormTemplate(String name, List<FieldTemplate<?>> fieldTemplates, MessageSource messageSource) {
        this.name = name;
        this.fieldTemplates = fieldTemplates;
        this.messageSource = messageSource;
    }

    public void validate(Object dto, Locale locale, HttpServletRequest httpRequest) throws Exception {
        FormDataValidationException ex = new FormDataValidationException();
        /*
            در بخش اعتبارسنچی ابتدا فیلدها براساس اولیت اعتبارسنجی مرتب می شوند
            مرتب‌سازی برای کنترل سناریوهایی است که ترتیب اعتبارسنجی فیلدها مهم است (مانند فیلد کپچا که باید پیش از همه اعتبارسنجی شود)
         */
        for (InputFieldTemplate<?, ?> inputFieldTemplate : this.getFieldTemplates(Map.of(), InputFieldTemplate.class).stream().sorted(Comparator.comparingInt(InputFieldTemplate::getValidationOrder)).collect(Collectors.toList())) {
            Object value = ReflectionUtils.getPropertyValue(dto, inputFieldTemplate.getName());
            inputFieldTemplate.validate(this, value, locale, httpRequest, ex);
        }
        if (!MapUtils.isEmpty(ex.getInvalidFields())) {
            throw ex;
        }
    }

    /**
     * اعتبارسنجی داده های ارسالی
     */
    public void validate(String flowToken, Map<String, String> flowData, Map<String, String> formData, Locale locale) throws Exception {
        Set<String> allowedFieldNames = this.getAllowedInputFields(flowData, locale);
        Set<String> illegalFields = new HashSet<>(formData.keySet());
        illegalFields.removeAll(allowedFieldNames);
        if (!illegalFields.isEmpty()) {
            if (this.isFailOnUnknownSubmitFields()) {
                FormDataValidationException ex = new FormDataValidationException();
                illegalFields.forEach(illegalField -> ex.addFieldError(illegalField, "illegal field"));
                throw ex;
            } else {
                log.warn("illegal fields[{}] exist in form [{}].", illegalFields, this.getName());
            }
        }
        FormDataValidationException ex = new FormDataValidationException();
        /*
            در بخش اعتبارسنچی ابتدا فیلدها براساس اولیت اعتبارسنجی مرتب می شوند
            مرتب‌سازی برای کنترل سناریوهایی است که ترتیب اعتبارسنجی فیلدها مهم است (مانند فیلد کپچا که باید پیش از همه اعتبارسنجی شود)
         */
        for (InputFieldTemplate<?, ?> inputFieldTemplate : this.getFieldTemplates(flowData, InputFieldTemplate.class).stream().sorted(Comparator.comparingInt(InputFieldTemplate::getValidationOrder)).collect(Collectors.toList())) {
            inputFieldTemplate.validate(flowToken, this, flowData, formData, locale, ex);
        }
        if (!MapUtils.isEmpty(ex.getInvalidFields())) {
            throw ex;
        }
    }

    public <T extends FieldTemplate<?>> List<T> getFieldTemplates(Map<String, String> args, Class<T> superClass) {
        List<T> fieldTemplates = new ArrayList<>();
        for (FieldTemplate<?> fieldTemplate : this.getFieldTemplates(args)) {
            if (superClass.isAssignableFrom(fieldTemplate.getClass())) {
                fieldTemplates.add((T) fieldTemplate);
            }
        }
        return fieldTemplates;
    }

    /**
     * محاسبه لیست فیلدهایی که استفاده کننده مجاز به ارسال دیتا برای آن هاست
     * این فیلدها شامل تمامی اینپوت فیلدهایی است که فعال باشند
     */
    public Set<String> getAllowedInputFields(Map<String, String> args, Locale locale) throws Exception {
        List<InputFieldTemplate> inputFieldTemplates = this.getFieldTemplates(args, InputFieldTemplate.class);
        Set<String> allowedFieldNames = new HashSet<>();
        for (InputFieldTemplate<?, ?> inputFieldTemplate : inputFieldTemplates) {
            Boolean disable = FormUtils.getFieldBooleanProperty(this, inputFieldTemplate, "disable", locale, args, null);
            if (!Boolean.TRUE.equals(disable)) {
                allowedFieldNames.add(inputFieldTemplate.getName());
            }
        }
        return allowedFieldNames;
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

    public void setShowExpression(String showExpression) {
        this.showExpression = new SpelExpressionParser(new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE, null)).parseExpression(showExpression);
    }
}
