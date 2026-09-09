bard-form
=========

[![Maven Central](https://img.shields.io/badge/maven--central-6.1.4-blue.svg)](https://repo1.maven.org/maven2/org/bardframework/form/bard-form/)
[![License](http://img.shields.io/:license-apache-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

**The template engine.** Turns a declarative form definition into a concrete
[`BardForm`](../bard-form-common) for a given user, locale and data set — and validates what comes
back.

Part of [**Bard Form &amp; Flow**](../README.md) · [Bard Framework](https://github.com/bardframework)

```xml
<dependency>
    <groupId>org.bardframework.form</groupId>
    <artifactId>bard-form</artifactId>
    <version>5.6.4</version>
</dependency>
```

## Template vs. field

```
FormTemplate   ──fillForm(values, args, locale)──►  BardForm
FieldTemplate  ──toField(...)───────────────────►  Field
```

A **template** is a long-lived bean: this form has these fields, in this order, under these
conditions. A **field** is the per-request result: this user, this locale, these values.

Nothing about the rendered form is hard-coded in the template — titles, placeholders, `required`,
`disable` and every type-specific property are resolved at `toField` time from the `MessageSource`.

## Defining a form

```xml
<bean name="customerForm" class="org.bardframework.form.FormTemplate"
      c:name="customer"
      c:messageSource-ref="messageSource"
      p:dtoClass="com.example.CustomerDto">
    <constructor-arg name="fieldTemplates">
        <util:list>
            <bean class="org.bardframework.form.field.input.TextFieldTemplate"        c:name="firstName"/>
            <bean class="org.bardframework.form.field.input.PhoneNumberFieldTemplate" c:name="mobile"/>
            <bean class="org.bardframework.form.field.input.DateFieldTemplate"        c:name="birthDate"/>
            <bean class="org.bardframework.form.field.view.DividerFieldTemplate"      c:name="sep1"/>
            <bean class="org.bardframework.form.field.input.TextAreaFieldTemplate"    c:name="notes"/>
        </util:list>
    </constructor-arg>
</bean>
```

Java configuration works identically — `FormTemplate` also accepts a
`Supplier<List<FieldTemplate<?>>>` when the field list must be computed.

## Where the text comes from

`FormUtils` resolves properties from the `MessageSource` with a fallback cascade:

```
field.<form>.<field>.<property>     most specific
field.<field>.<property>            any form
field.<property>                    global default
<the template's own default value>
```

```properties
form.customer.title=مشتری
form.customer.submitLabel=ثبت
field.customer.firstName.title=نام
field.customer.firstName.required=true
field.mobile.title=شماره موبایل        # applies to "mobile" in every form
```

Two things follow. First, **behaviour is configuration**: `required`, `disable`, `maxLength`,
`minValue` are read exactly like `title`, so what a form demands can change without a rebuild.
Second, values support `::placeholder::` substitution from the current data map, so a title can name
the user or quote a number from an earlier step:

```properties
form.verify.description=کد به شماره ::mobile:: ارسال شد
```

With `WildcardReloadableMessageSource` from
[`common-web`](https://github.com/bardframework/bard-commons) and a non-zero `cacheSeconds`, edits
take effect while the application runs.

## Conditional fields

```xml
<bean class="...TextFieldTemplate" c:name="companyName"
      p:showExpression="#root['customerType'] == 'LEGAL'"/>

<bean class="...TextFieldTemplate" c:name="nationalId"
      p:disableExpression="#root['verified'] == true"/>
```

SpEL, evaluated against the current data map. A field whose `showExpression` is false is absent from
the response **and rejected on submit** — invisible and unsubmittable are the same thing.

`FormTemplate` has its own `showExpression`, used by [`bard-flow`](../bard-flow) to skip whole steps.

## Validation

```java
FormUtils.validate(flowToken, formTemplate, flowData, formData, locale, httpRequest, httpResponse);
```

1. Compute the allowed input fields — every enabled input template that passes `showExpression`.
2. Reject any submitted key outside that set (`failOnUnknownSubmitFields`, default `true`).
3. Validate the rest **ordered by `getValidationOrder()`**, so a captcha (`Integer.MIN_VALUE`) is
   always checked before anything costly.
4. Collect per-field errors and throw `FormDataValidationException` with all of them at once.

Each `InputFieldTemplateAbstract` implements `isValid(flowToken, field, value, flowData)` and
`toValue(Object)` — the latter converting the raw JSON value into the field's typed value.

Nested `formTemplates` are validated recursively, with the child's data taken from a nested object
under the child form's name.

## Select options

```xml
<bean class="...SingleSelectFieldTemplate" c:name="province">
    <property name="optionDataSource">
        <bean class="org.bardframework.form.field.option.BaseModelOptionDataSource"
              c:service-ref="provinceService"/>
    </property>
</bean>
```

| `OptionDataSource` | Options come from |
| --- | --- |
| `ConstantOptionDataSource` | A fixed configured list. |
| `I18nConstantOptionDataSource` | Ids in configuration, titles from the `MessageSource`. |
| `EnumOptionDataSource` | A Java enum. |
| `PropertiesFileOptionDataSource` | A `.properties` file. |
| `NumberOptionDataSource` | A numeric range. |
| `BaseModelOptionDataSource` | A [`bard-crud`](https://github.com/bardframework/bard-crud) service. |
| `CachableOptionDataSource` | Decorator — caches any of the above. |

`SortBy` controls the ordering of the produced `SelectOption` list.

## Value providers

`FieldValueProvider` pre-fills a field when the form is built: `ConstantValueProvider`,
`FileFieldDataProvider`, `ImageFieldDataProvider` (which resolve a stored file into a downloadable
reference).

## Phone numbers

`PhoneNumberFieldTemplate` uses `PhoneNumberParser`, `PhoneNumber`, `Country` and
`CountryPhoneNumberInfo` (libphonenumber underneath) to validate and normalise per country, so the
same field works for `09121234567` and `+989121234567`.

## Template catalogue

One `*Template` per wire type in [`bard-form-common`](../bard-form-common) — input, view and filter
families alike. Abstract bases (`InputFieldTemplateAbstract`, `MultiSelectFieldTemplateAbstract`,
`FileUploadFieldTemplateAbstract`, `IpFieldTemplateAbstract`, `IpFilterFieldTemplateAbstract`) are
there to extend when you need a variant.

## Testing

A `test-jar` classifier publishes data providers for the common field types
(`TextFieldDataProvider`, `NumberFieldDataProvider`, `DateFieldDataProvider`,
`SingleSelectFieldDataProvider`, `ListFieldDataProvider`, …) plus `BardFormTestConfiguration`, so a
custom field template can be tested against the same contract as the built-in ones.

## License

Apache License 2.0.
