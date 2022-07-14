package org.bardframework.form.flow;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FlowData {

    protected static final String LANGUAGE_FIELD_NAME = "S_LANG_TAG";
    protected Map<String, String> flowData = new HashMap<>();
    protected int nextStepIndex;

    @JsonIgnore
    public Locale getLocale() {
        String languageTag = flowData.get(LANGUAGE_FIELD_NAME);
        return null == languageTag ? null : Locale.forLanguageTag(languageTag);
    }

    public FlowData setLocale(Locale locale) {
        this.flowData.put(LANGUAGE_FIELD_NAME, locale.getLanguage());
        return this;
    }

    public Map<String, String> getFlowData() {
        return flowData;
    }

    public void setFlowData(Map<String, String> flowData) {
        this.flowData = flowData;
    }

    public int getNextStepIndex() {
        return nextStepIndex;
    }

    public void setNextStepIndex(int nextStepIndex) {
        this.nextStepIndex = nextStepIndex;
    }
}
