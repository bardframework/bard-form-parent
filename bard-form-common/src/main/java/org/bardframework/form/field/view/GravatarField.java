package org.bardframework.form.field.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.bardframework.form.field.Field;
import org.bardframework.form.field.FieldType;

@Slf4j
@NoArgsConstructor
@Getter
@Setter
@ToString
public class GravatarField extends Field {
    private String identifier;
    private Long size;
    private Boolean circle;

    public FieldType getType() {
        return ViewFieldType.GRAVATAR;
    }
}
