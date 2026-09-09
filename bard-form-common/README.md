bard-form-common
================

[![Maven Central](https://img.shields.io/badge/maven--central-5.6.4-blue.svg)](https://repo1.maven.org/maven2/org/bardframework/form/bard-form-common/)
[![License](http://img.shields.io/:license-apache-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

**The wire format.** Plain data classes describing a form, a table, a field and a filter — the
vocabulary the server and every client agree on.

Jackson is the only dependency. No Spring, no servlet API. Put this on the classpath of anything that
needs to speak the protocol: a backend, a test harness, another JVM service consuming a Bard API.

Part of [**Bard Form &amp; Flow**](../README.md) · [Bard Framework](https://github.com/bardframework)

```xml
<dependency>
    <groupId>org.bardframework.form</groupId>
    <artifactId>bard-form-common</artifactId>
    <version>5.6.4</version>
</dependency>
```

## What is in here

```
org.bardframework.form
├── BardForm                   a form: metadata + fields + nested forms
├── field
│   ├── Field                  base — name, title, info, description, @type
│   ├── input/                 34 input types (the user supplies a value)
│   ├── view/                  9 display-only types
│   └── filter/                11 filter types (the user narrows a result set)
├── model
│   ├── SelectOption           an option in a select field
│   └── filter/                the Filter hierarchy
org.bardframework.table
├── TableModel                 a table: metadata + headers + filter/save/update forms
├── TableData                  the rows
└── header/                    header types
org.bardframework.flow
└── FlowResponse               a step in a conversation
```

## Polymorphism on the wire

`Field` and `TableHeader` are serialised with:

```java
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, property = "@type")
```

so every field carries its concrete type and the client can pick a widget:

```json
{
  "name": "signup",
  "title": "ثبت‌نام",
  "submitLabel": "ادامه",
  "fields": [
    { "@type": ".field.input.PhoneNumberField", "name": "mobile", "title": "شماره موبایل", "required": true },
    { "@type": ".field.input.CaptchaField", "name": "captcha", "value": "data:image/png;base64,…" }
  ]
}
```

## BardForm

| Property | Meaning |
| --- | --- |
| `name`, `title`, `description` | Identity and headings. |
| `submitLabel`, `confirmMessage` | Submit button text; optional confirmation prompt. |
| `submitPristineInputs` | Send untouched inputs too, or only changed ones. |
| `submitEmptyInputs` | Send `null`/empty values, or omit them. |
| `autoSubmitDelaySeconds` | Submit automatically after N seconds — used by OTP screens. |
| `fieldDescriptionShowType` | `HINT` or `TOOLTIP`. |
| `nestedFormShowType` | `MAIN_FORM`, `WIZARD` or `TAB` — how child forms are laid out. |
| `fields`, `forms` | Fields, and nested forms. |

## Field families

**Input** (`InputField<T>` — adds `value`, `required`, `disable`, `placeholder`, `errorMessage`):

`TextField` · `TextAreaField` · `NumberField` · `PasswordField` · `NewPasswordField` ·
`CheckBoxField` · `SwitchButtonField` · `DateField` · `DateTimeField` · `TimeField` ·
`SingleSelectField` · `MultiSelectField` · `SingleSelectSearchableField` ·
`MultiSelectSearchableField` · `ListField` · `PhoneNumberField` · `Ip4Field` · `Ip6Field` ·
`CardField` · `FileUploadField` · `ImageUploadField` · `AvatarSelectField` · `IconSelectField` ·
`LocationSelectField` · `CountrySelectField` · `CountryMultiSelectField` · `LocaleSelectField` ·
`LocaleMultiSelectField` · `TimeZoneSelectField` · `HtmlEditorField` · `WysiwygField` ·
`CaptchaField` · `OtpField` · `SsotpField`

**View** (display only): `ReadonlyField` · `MessageField` · `DividerField` · `HtmlViewField` ·
`ImageViewField` · `AvatarViewField` · `GravatarField` · `FileDownloadField` · `PaymentField`

**Filter** (produce a `Filter`): `TextFilterField` · `LongFilterField` · `DateFilterField` ·
`DateTimeFilterField` · `TimeFilterField` · `Ip4FilterField` · `Ip6FilterField` ·
`SingleSelectFilterField` · `MultiSelectFilterField` · `SingleSelectSearchableFilterField` ·
`MultiSelectSearchableFilterField`

## Filters

The query vocabulary, shared with [`bard-crud`](https://github.com/bardframework/bard-crud) criteria
objects:

```
Filter<T, F>            equals · notEquals · specified · in · notIn
  └── RangeFilter<T, F>   + from · to
        ├── StringFilter    + contains · doesNotContain · startWith · endWith
        └── NumberRangeFilter
```

Concrete types: `StringFilter` · `IdFilter` · `BooleanFilter` · `EnumFilter` · `ByteFilter` ·
`ShortFilter` · `IntegerFilter` · `LongFilter` · `FloatFilter` · `DoubleFilter` ·
`BigDecimalFilter` · `BigIntegerFilter` · `InstantFilter` · `LocalTimeFilter` · `DurationFilter` ·
`StringRangeFilter`

They bind from query strings as `field.equals=`, `field.contains=`, `field.from=`, and every setter
returns `this` for chaining.

`isEmpty()` reports whether any restriction was set — the property
[`crud-querydsl-sql`](https://github.com/bardframework/bard-crud) relies on to reject a filter that
would match everything.

## Table types

`TableModel` carries `name`, `title`, `fetchSize`, the `headers`, the `filterForm` / `saveForm` /
`updateForm`, and the capability flags `pageable`, `preload`, `export`, `print`, `delete`,
`hideColumn`, `collapseFilterForm`.

`TableHeader` subtypes: `StringHeader` · `NumberHeader` · `DateHeader` · `DateTimeHeader` ·
`TimeHeader` · `ImageHeader` · `AvatarHeader` · `Ip4Header` · `Ip6Header`.

## FlowResponse

```java
String            id;               // X-Flow-Token for the next call; absent when finished
BardForm          form;             // the form to render
Boolean           finished;
Integer           resetDelaySeconds;
int               steps, current;   // progress indicator
Map<String,String> fieldErrors;     // field name → message
List<String>      errors;           // form-level messages
```

## Building a client

Everything a renderer needs is here. Map `@type` to a widget, read `value`, post a flat
`{ fieldName: value }` object back. See
[`@bard/angular`](https://github.com/bardframework/bard-angular) and
[bard-android](https://github.com/bardframework/bard-android) for two working implementations.

## License

Apache License 2.0.
