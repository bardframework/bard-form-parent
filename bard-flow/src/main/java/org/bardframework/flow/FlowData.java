package org.bardframework.flow;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FlowData {

    public static final String LANGUAGE_FIELD_NAME = "S_LANG_TAG";
    protected Map<String, String> data = new HashMap<>();
    protected int nextStepIndex;

    @JsonIgnore
    public Locale getLocale() {
        String languageTag = data.get(LANGUAGE_FIELD_NAME);
        return null == languageTag ? null : Locale.forLanguageTag(languageTag);
    }

    public FlowData setLocale(Locale locale) {
        this.data.put(LANGUAGE_FIELD_NAME, locale.getLanguage());
        return this;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public int getNextStepIndex() {
        return nextStepIndex;
    }

    public void setNextStepIndex(int nextStepIndex) {
        this.nextStepIndex = nextStepIndex;
    }
}
