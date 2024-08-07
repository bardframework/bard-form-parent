package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AvatarSelectFieldTemplate extends ImageUploadFieldTemplateAbstract<AvatarSelectField> {

    public AvatarSelectFieldTemplate(String name) {
        super(name);
    }
}
