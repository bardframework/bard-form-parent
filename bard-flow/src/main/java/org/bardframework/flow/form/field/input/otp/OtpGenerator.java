package org.bardframework.flow.form.field.input.otp;

public interface OtpGenerator<T> {

    T generate();

    int getLength();

    Boolean isNumber();
}