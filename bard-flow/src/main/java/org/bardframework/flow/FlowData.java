package org.bardframework.flow;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Getter
@Setter
public class FlowData {

    public static final String LANGUAGE_FIELD_NAME = "S_LANG_TAG";
    protected Map<String, Object> data = new HashMap<>();
    protected int currentFormIndex;

    @JsonIgnore
    public Locale getLocale() {
        Object languageTag = data.get(LANGUAGE_FIELD_NAME);
        return null == languageTag ? null : Locale.forLanguageTag(languageTag.toString());
    }

    public FlowData setLocale(Locale locale) {
        this.data.put(LANGUAGE_FIELD_NAME, locale.getLanguage());
        return this;
    }

}
