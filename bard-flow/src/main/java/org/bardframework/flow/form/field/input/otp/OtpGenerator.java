package org.bardframework.flow.form.field.input.otp;

public interface OtpGenerator<T> {

    T generate() throws Exception;

    int getLength();

    Boolean isNumber();
}