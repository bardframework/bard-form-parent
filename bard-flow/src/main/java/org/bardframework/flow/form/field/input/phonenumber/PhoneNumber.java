package org.bardframework.flow.form.field.input.phonenumber;

public class PhoneNumber {
    private String rawValue;
    private String countryAlphaCode;
    private int countryCode;
    private long number;

    public String getRawValue() {
        return rawValue;
    }

    public void setRawValue(String rawValue) {
        this.rawValue = rawValue;
    }

    public String getCountryAlphaCode() {
        return countryAlphaCode;
    }

    public void setCountryAlphaCode(String countryAlphaCode) {
        this.countryAlphaCode = countryAlphaCode;
    }

    public int getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(int countryCode) {
        this.countryCode = countryCode;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getFullNumber() {
        return "%d%d".formatted(this.countryCode, this.number);
    }
}
