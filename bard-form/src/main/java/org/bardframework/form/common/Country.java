package org.bardframework.form.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
public class Country {
    private String defaultName;
    private String alpha2Code;
    private String callingCode;
    private List<String> timeZones = new ArrayList<>();

    public List<String> getTimeZones() {
        Collections.sort(timeZones);
        return timeZones;
    }
}
