package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import org.bardframework.form.field.FieldType;

@Getter
@Setter
public class AvatarSelectField extends ImageUploadField {
    private Boolean cropper;

    @Override
    public FieldType getType() {
        return InputFieldType.AVATAR_SELECT;
    }
}