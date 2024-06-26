package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.form.field.FieldType;

@Getter
@Setter
@ToString
public class NewPasswordField extends PasswordField {

    private Boolean showConfirmPassword;

    public Boolean getShowConfirmPassword() {
        if (null == showConfirmPassword) {
            showConfirmPassword = false;
        }
        return showConfirmPassword;
    }

    @Override
    public FieldType getType() {
        return InputFieldType.NEW_PASSWORD;
    }
}