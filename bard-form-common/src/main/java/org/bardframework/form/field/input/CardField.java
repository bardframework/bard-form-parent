package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.form.field.FieldType;

@Getter
@Setter
@ToString
public class CardField extends InputField<String> {

    private String subTitle;
    private String avatarUrl;
    private String selectLabel;

    public CardField() {
    }

    public CardField(String name, String value) {
        super(name);
        this.setValue(name);
    }

    @Override
    public FieldType getType() {
        return InputFieldType.CARD;
    }

    @Override
    public String toString(String value) {
        return value;
    }
}