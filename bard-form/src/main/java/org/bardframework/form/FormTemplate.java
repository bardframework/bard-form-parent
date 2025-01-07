package org.bardframework.form;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.bardframework.form.field.Field;
import org.bardframework.form.field.FieldTemplate;
import org.bardframework.form.field.input.InputField;
import org.bardframework.form.field.input.InputFieldTemplateAbstract;
import org.bardframework.form.field.view.ReadonlyField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Setter
@ToString
public class FormTemplate {

    protected final String name;
    protected final MessageSource messageSource;
    protected final Supplier<List<FieldTemplate<?>>> fieldTemplatesSupplier;
    protected FieldDescriptionShowType descriptionShowType = FieldDescriptionShowType.HINT;
    protected Class<?> dtoClass;
    protected boolean failOnUnknownSubmitFields = true;
    protected Boolean submitPristineInputs;
    protected Boolean submitEmptyInputs;
    protected Expression showExpression = null;
    protected List<FormTemplate> formTemplates = new ArrayList<>();
    protected NestedFormShowType nestedFormShowType = NestedFormShowType.MAIN_FORM;
    protected Integer autoSubmitDelaySeconds = null;

    @Autowired
    public FormTemplate(String name, List<FieldTemplate<?>> fieldTemplates, MessageSource messageSource) {
        this(name, () -> fieldTemplates, messageSource);
    }

    public FormTemplate(String name, Supplier<List<FieldTemplate<?>>> fieldTemplatesSupplier, MessageSource messageSource) {
        this.name = name;
        this.fieldTemplatesSupplier = fieldTemplatesSupplier;
        this.messageSource = messageSource;
    }

    public <T extends FieldTemplate<?>> List<T> getFieldTemplates(Map<String, Object> flowData, Map<String, Object> args, Class<T> superClass, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        List<T> fieldTemplates = new ArrayList<>();
        for (FieldTemplate<?> fieldTemplate : this.getFieldTemplates(flowData, args, httpRequest, httpResponse)) {
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
    public Set<String> getAllowedInputFields(Map<String, Object> flowData, Map<String, Object> args, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        List<InputFieldTemplateAbstract> inputFieldTemplates = this.getFieldTemplates(flowData, args, InputFieldTemplateAbstract.class, httpRequest, httpResponse);
        Set<String> allowedFieldNames = new HashSet<>();
        for (InputFieldTemplateAbstract<?, ?> inputFieldTemplate : inputFieldTemplates) {
            Boolean disable = FormUtils.getFieldBooleanProperty(this, inputFieldTemplate, "disable", locale, args, null);
            if (!Boolean.TRUE.equals(disable)) {
                allowedFieldNames.add(inputFieldTemplate.getName());
            }
        }
        return allowedFieldNames;
    }

    public boolean mustShow(Map<String, Object> args) {
        return null == showExpression || Boolean.TRUE.equals(showExpression.getValue(new StandardEvaluationContext(args), Boolean.class));
    }

    public List<FieldTemplate<?>> getFieldTemplates(Map<String, Object> flowData, Map<String, Object> args, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        return this.getFieldTemplates().stream().filter(fieldTemplate -> fieldTemplate.mustShow(flowData)).collect(Collectors.toList());
    }

    public List<FieldTemplate<?>> getFieldTemplates() {
        return this.fieldTemplatesSupplier.get();
    }

    public FieldTemplate<?> getField(String name, Map<String, Object> flowData, Map<String, Object> args, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        return this.getFieldTemplates(flowData, args, httpRequest, httpResponse).stream().filter(field -> field.getName().equals(name)).findFirst().orElse(null);
    }

    public void setShowExpression(String showExpression) {
        this.showExpression = new SpelExpressionParser(new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE, null)).parseExpression(showExpression);
    }

    public <F extends BardForm, T> void fillForm(F form, Map<String, Object> values, Map<String, Object> args, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        form.setName(this.getName());
        form.setTitle(this.getTitle(httpRequest, values, args, locale));
        form.setDescription(FormUtils.getFormStringProperty(this, "description", locale, args, null));
        form.setConfirmMessage(FormUtils.getFormStringProperty(this, "confirmMessage", locale, args, null));
        form.setSubmitLabel(FormUtils.getFormStringProperty(this, "submitLabel", locale, args, null));
        form.setSubmitPristineInputs(FormUtils.getFormBooleanProperty(this, "submitPristineInputs", locale, args, this.getSubmitPristineInputs()));
        form.setSubmitEmptyInputs(FormUtils.getFormBooleanProperty(this, "submitEmptyInputs", locale, args, this.getSubmitEmptyInputs()));
        form.setAutoSubmitDelaySeconds(FormUtils.getFormIntegerProperty(this, "autoSubmitDelaySeconds", locale, args, this.getAutoSubmitDelaySeconds()));
        form.setFieldDescriptionShowType(FormUtils.getFormEnumProperty(this, "fieldDescriptionShowType", FieldDescriptionShowType.class, locale, args, this.getDescriptionShowType()));
        form.setNestedFormShowType(FormUtils.getFormEnumProperty(this, "nestedFormShowType", NestedFormShowType.class, locale, args, this.getNestedFormShowType()));
        for (FieldTemplate<?> fieldTemplate : this.getFieldTemplates(values, args, httpRequest, httpResponse)) {
            Field field = fieldTemplate.toField(this, values, args, locale);
            Object value = values.get(fieldTemplate.getName());
            if (null == value) {
                value = args.get(fieldTemplate.getName());
            }
            try {
                if (field instanceof InputField<?> && null == ((InputField<?>) field).getValue()) {
                    InputFieldTemplateAbstract<?, T> inputFieldTemplate = (InputFieldTemplateAbstract<?, T>) fieldTemplate;
                    ((InputField<T>) field).setValue(inputFieldTemplate.toValue(value));
                } else if (field instanceof ReadonlyField) {
                    ((ReadonlyField) field).setValue(value);
                }
            } catch (Exception e) {
                log.error("error while filling form filed [{}].", fieldTemplate.getName(), e);
            }
            form.addField(field);
        }
        for (FormTemplate childFormTemplate : this.getFormTemplates()) {
            form.addForm(FormUtils.toForm(childFormTemplate, values, args, locale, httpRequest, httpResponse));
        }
    }

    protected String getTitle(HttpServletRequest httpRequest, Map<String, Object> values, Map<String, Object> args, Locale locale) {
        return FormUtils.getFormStringProperty(this, "title", locale, args, null);
    }
}
