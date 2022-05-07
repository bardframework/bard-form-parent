package org.bardframework.form.common.field.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.bardframework.form.common.FieldType;
import org.bardframework.form.common.field.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(name = "TEXT", value = TextField.class),
        @JsonSubTypes.Type(name = "PASSWORD", value = PasswordField.class),
        @JsonSubTypes.Type(name = "NEW_PASSWORD", value = NewPasswordField.class),
        @JsonSubTypes.Type(name = "LIST", value = ListField.class),
        @JsonSubTypes.Type(name = "NUMBER", value = NumberField.class),
        @JsonSubTypes.Type(name = "NUMBER_RANGE", value = NumberRangeField.class),
        @JsonSubTypes.Type(name = "DATE", value = DateField.class),
        @JsonSubTypes.Type(name = "DATE_RANGE", value = DateRangeField.class),
        @JsonSubTypes.Type(name = "SINGLE_SELECT", value = SingleSelectField.class),
        @JsonSubTypes.Type(name = "SINGLE_SELECT_SEARCHABLE", value = SingleSelectSearchableField.class),
        @JsonSubTypes.Type(name = "MULTI_SELECT", value = MultiSelectField.class),
        @JsonSubTypes.Type(name = "MULTI_SELECT_SEARCHABLE", value = MultiSelectSearchableField.class),
        @JsonSubTypes.Type(name = "TEXT_AREA", value = TextAreaField.class),
        @JsonSubTypes.Type(name = "FILE_UPLOAD", value = FileUploadField.class),
        @JsonSubTypes.Type(name = "IMAGE_UPLOAD", value = ImageUploadField.class),
        @JsonSubTypes.Type(name = "FILE", value = FileField.class),
        @JsonSubTypes.Type(name = "IMAGE", value = ImageField.class),
        @JsonSubTypes.Type(name = "AVATAR", value = AvatarField.class),
        @JsonSubTypes.Type(name = "DIVIDER", value = DividerField.class),
        @JsonSubTypes.Type(name = "CAPTCHA", value = CaptchaField.class),
        @JsonSubTypes.Type(name = "MESSAGE", value = MessageField.class),
        @JsonSubTypes.Type(name = "PAYMENT", value = PaymentField.class),
})
public abstract class Field {
    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public abstract FieldType<?> getType();

}
