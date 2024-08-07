package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageUploadFieldTemplate extends ImageUploadFieldTemplateAbstract<ImageUploadField> {

    protected ImageUploadFieldTemplate(String name) {
        super(name);
    }
}
