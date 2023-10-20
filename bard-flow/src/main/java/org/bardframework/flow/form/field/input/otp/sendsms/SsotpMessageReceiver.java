package org.bardframework.flow.form.field.input.otp.sendsms;

public interface SsotpMessageReceiver {

    void put(String identifier, String otp);

    String get(String identifier);

    String remove(String identifier);
}
