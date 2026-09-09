bard-table
==========

[![Maven Central](https://img.shields.io/badge/maven--central-6.1.4-blue.svg)](https://repo1.maven.org/maven2/org/bardframework/form/bard-table/)
[![License](http://img.shields.io/:license-apache-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

The template engine of [`bard-form`](../bard-form), applied to tables. One bean describes an entire
CRUD screen: the grid, its search form, its create dialog and its edit dialog.

Part of [**Bard Form &amp; Flow**](../README.md) · [Bard Framework](https://github.com/bardframework)

```xml
<dependency>
    <groupId>org.bardframework.form</groupId>
    <artifactId>bard-table</artifactId>
    <version>6.1.4</version>
</dependency>
```

## A whole screen in one bean

```xml
<bean name="CustomerPage" class="org.bardframework.table.TableTemplate"
      p:name="customer" p:pageable="true" p:preload="true" p:delete="true" p:export="true"
      c:modelClass="com.example.CustomerModel"
      c:messageSource-ref="messageSource">

    <property name="filterFormTemplate">
        <bean class="org.bardframework.form.FormTemplate" c:name="filter"
              c:messageSource-ref="messageSource"
              p:dtoClass="com.example.CustomerCriteria">
            <constructor-arg name="fieldTemplates">
                <util:list>
                    <bean class="org.bardframework.form.field.filter.TextFilterFieldTemplate" c:name="searchFilter"/>
                </util:list>
            </constructor-arg>
        </bean>
    </property>

    <property name="saveFormTemplate">   <bean parent="customerForm" c:name="save"/>   </property>
    <property name="updateFormTemplate"> <bean parent="customerForm" c:name="update"/> </property>

    <constructor-arg name="headerTemplates">
        <util:list>
            <bean class="org.bardframework.table.header.StringHeaderTemplate"    p:name="email"/>
            <bean class="org.bardframework.table.header.StringHeaderTemplate"    p:name="firstName"/>
            <bean class="org.bardframework.table.header.LocalDateHeaderTemplate" p:name="birthDate"/>
            <bean class="org.bardframework.table.header.EnumHeaderTemplate"      p:name="status"/>
            <bean class="org.bardframework.table.header.StringPropertyPathHeaderTemplate"
                  p:name="owner.company.title"/>
        </util:list>
    </constructor-arg>
</bean>
```

Serve it with `TableModelRestController` from
[`crud-table`](https://github.com/bardframework/bard-crud) and the client renders the page with no
page-specific code.

## HeaderTemplate

```java
public abstract class HeaderTemplate<M, H extends TableHeader, T> extends TableHeader {
    protected T      getValue(M model);                                  // reflection by name
    protected Object format(T value, MessageSource ms, Locale locale);   // for JSON
    protected Object formatForExport(T value, MessageSource ms, Locale locale); // for Excel
}
```

The two formatting hooks are the point. A `LocalDate` can be a Jalali string on screen and a real
date cell in the spreadsheet; an enum can be a translated label on screen and the same label in
Excel; a byte count can be "1.4 MB" on screen and a number for sorting.

## Available headers

**Text and numbers** — `StringHeaderTemplate`, `StringPropertyPathHeaderTemplate`,
`NumberHeaderTemplate`, `IntegerHeaderTemplate`, `LongHeaderTemplate`, `ShortHeaderTemplate`,
`ByteHeaderTemplate`, `DoubleHeaderTemplate`, `BooleanHeaderTemplate`.

**Dates and times** — `LocalDateHeaderTemplate`, `LocalDateTimeHeaderTemplate`,
`LocalTimeHeaderTemplate`, `DateTimePropertyPathHeaderTemplate`, and the epoch-millis variants
`EpochMillsDateHeaderTemplate`, `EpochMillsDateTimeHeaderTemplate`, `EpochMillsTimeHeaderTemplate`.
Persian locales render through
[`jalali-date`](https://github.com/bardframework/jalali-date).

**Enums** — `EnumHeaderTemplate`, `EnumPropertyPathHeaderTemplate` (raw), `I18nHeaderTemplate`,
`I18nBasedHeaderTemplate` (translated through the `MessageSource`).

**Media** — `ImageHeaderTemplate`, `AvatarHeaderTemplate`.

**Other** — `Ip4HeaderTemplate`, `Ip6HeaderTemplate`, `DurationPropertyPathHeaderTemplate`,
`FileSizePropertyPathHeaderTemplate`, `BaseModelHeaderTemplate` (renders a nested model).

`*PropertyPathHeaderTemplate` variants read nested paths — `p:name="owner.company.title"` — so a grid
can show joined data without a flattened DTO.

## i18n keys

```properties
table.customer.title=مشتریان
table.customer.description=فهرست مشتریان
table.customer.pageable=true
table.customer.preload=true

header.customer.email.title=رایانامه
header.customer.email.sortable=true
header.customer.birthDate.title=تاریخ تولد
header.customer.lastName.hidden=true
```

Same cascade as forms: `header.<table>.<column>.<property>` falls back to
`header.<column>.<property>` and then to the template's own default. Per-column `hidden`, `sortable`,
`movable` and `sticky` are all resolved this way, so column behaviour is configuration.

`TableUtils.toTable(template, args, locale, request, response)` produces the `TableModel`.

## License

Apache License 2.0.
