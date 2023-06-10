package org.bardframework.table.header;

public class BooleanHeaderTemplate<M> extends I18nBasedHeaderTemplate<M, Boolean> {

    @Override
    protected String getI18nKey(Boolean value) {
        return "Boolean." + value;
    }
}
