package org.bardframework.form.common;

public enum FieldTypeBase implements FieldType<FieldTypeBase> {
    TEXT,
    PASSWORD,
    NEW_PASSWORD,
    LIST,
    NUMBER,
    NUMBER_RANGE,
    DATE,
    DATE_RANGE,
    SINGLE_CHECKBOX,
    SINGLE_SELECT,
    SINGLE_SELECT_SEARCHABLE,
    MULTI_SELECT,
    MULTI_SELECT_SEARCHABLE,
    TEXT_AREA,
    IMAGE_UPLOAD,
    FILE_UPLOAD,
    READONLY,
    FILE,
    IMAGE,
    AVATAR,
    DIVIDER,
    CAPTCHA,
    MESSAGE,
    PAYMENT
}
