package org.bardframework.table.header;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@ToString
public class I18nHeaderTemplate<M> extends I18nBasedHeaderTemplate<M, String> {

    @Override
    protected String getI18nKey(String value) {
        return value;
    }
}