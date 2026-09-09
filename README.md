Bard Form &amp; Flow
==================

[![Maven Central](https://img.shields.io/badge/maven--central-6.1.4-blue.svg)](https://repo1.maven.org/maven2/org/bardframework/form/)
[![License](http://img.shields.io/:license-apache-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

**Server-driven forms and tables, and a flow engine for stateless applications.**

The server describes a form; the client renders it. The server owns every label, every validation
rule, every visibility condition and — in a flow — which step comes next. Clients hold no state and
make no decisions.

`groupId` `org.bardframework.form`, version **6.1.4** (managed by [`bard-bom`](https://github.com/bardframework/bard-bom)).

## Artifacts

| Artifact | Depends on | Contents |
| --- | --- | --- |
| `bard-form-common` | Jackson only | The **wire format**: `BardForm`, `Field` and its ~60 subclasses, `TableModel`, `TableHeader`, and the `Filter` hierarchy. Put this on the classpath of anything that needs to talk the protocol. |
| `bard-form` | `bard-form-common`, Spring Context | The **template engine**: `FormTemplate`, `FieldTemplate`, i18n resolution, SpEL conditions, option data sources. |
| `bard-table` | `bard-form` | `TableTemplate` and `HeaderTemplate` — the same idea applied to tables. |
| `bard-flow` | `bard-form` | The **flow engine**: `FlowHandler`, `FlowController`, form processors, and self-contained OTP/captcha field templates. |
| `bard-flow-redis` | `bard-flow`, Spring Data Redis | `FlowDataRepositoryRedis` — conversation state in Redis with a TTL. |

```xml
<dependency>
    <groupId>org.bardframework.form</groupId>
    <artifactId>bard-flow</artifactId>
</dependency>
<dependency>
    <groupId>org.bardframework.form</groupId>
    <artifactId>bard-flow-redis</artifactId>
</dependency>
```

---

## Part 1 — Forms

### Three layers

```
FormTemplate  ──fillForm()──►  BardForm  ──JSON──►  <bard-form> / FormFragment
FieldTemplate ──toField()───►  Field
(server-side definition)       (wire model)         (client widget)
```

A **template** is a long-lived, immutable-ish definition — usually a Spring bean. A **field** is the
per-request, per-user, per-locale result of evaluating that template.

### Defining a form

Templates are ordinary beans, so XML is the most compact form (Java config works identically):

```xml
<bean name="customerForm" class="org.bardframework.form.FormTemplate"
      c:name="customer"
      c:messageSource-ref="messageSource"
      p:dtoClass="com.example.CustomerDto">
    <constructor-arg name="fieldTemplates">
        <util:list>
            <bean class="org.bardframework.form.field.input.TextFieldTemplate"     c:name="firstName"/>
            <bean class="org.bardframework.form.field.input.PhoneNumberFieldTemplate" c:name="mobile"/>
            <bean class="org.bardframework.form.field.input.TextAreaFieldTemplate" c:name="description"/>
        </util:list>
    </constructor-arg>
</bean>
```

Load them with `@ImportResource({"classpath*:**/**Page.xml"})` so each feature package can keep its
form next to its code.

### Where the text comes from

No labels appear above. They come from the `MessageSource`, using keys built from
`<keyType>.<form>.<field>.<property>` with a fallback cascade
([`FormUtils.getString`](bard-form/src/main/java/org/bardframework/form/FormUtils.java)):

```properties
# customer/i18n_fa.properties
form.customer.title=مشتری
form.customer.submitLabel=ثبت
field.customer.firstName.title=نام
field.customer.firstName.placeholder=نام خود را وارد کنید
field.customer.firstName.required=true
field.customer.mobile.title=شماره موبایل
field.customer.mobile.errorMessage=شماره موبایل معتبر نیست
```

Two consequences worth internalising:

1. **Behaviour, not just translation.** `required`, `disable`, `maxLength`, `pageable` are read the
   same way as `title`. You can change what a form demands by editing a `.properties` file.
2. **Fallback.** A key without the form name (`field.firstName.title`) applies to every form that
   does not override it, so shared fields are defined once.

Values support `::placeholder::` substitution from the current data map (`StringTemplateUtils`).

### Conditional fields

Both `FormTemplate` and `FieldTemplate` accept SpEL expressions evaluated against the current data:

```xml
<bean class="...TextFieldTemplate" c:name="companyName"
      p:showExpression="#root['customerType'] == 'LEGAL'"/>

<bean class="...TextFieldTemplate" c:name="nationalId"
      p:disableExpression="#root['verified'] == true"/>
```

A hidden field is not merely invisible — it is absent from the response *and* rejected on submit.

### Validation

```java
FormUtils.validate(flowToken, formTemplate, flowData, formData, locale, request, response);
```

1. Compute the allowed input fields (all enabled input templates, after `showExpression`).
2. Any submitted key outside that set → `FormDataValidationException`, unless
   `failOnUnknownSubmitFields=false`.
3. Validate remaining fields **sorted by `getValidationOrder()`** — this is why a captcha (order
   `Integer.MIN_VALUE`) is always checked before anything expensive.

Errors accumulate per field and are returned as a `fieldErrors` map, so the client can highlight
every bad input at once.

### Field catalogue

`bard-form-common` defines the wire types; `bard-form` defines a matching `*Template` for each.

**Input fields** (`InputFieldType`) — the user provides a value:

`TEXT` · `TEXT_AREA` · `NUMBER` · `PASSWORD` · `NEW_PASSWORD` · `CHECKBOX` · `SWITCH_BUTTON` ·
`DATE` · `DATE_TIME` · `TIME` · `SINGLE_SELECT` · `MULTI_SELECT` · `SINGLE_SELECT_SEARCHABLE` ·
`MULTI_SELECT_SEARCHABLE` · `LIST` · `PHONE_NUMBER` · `IP4` · `IP6` · `CARD` · `FILE_UPLOAD` ·
`IMAGE_UPLOAD` · `AVATAR_SELECT` · `ICON_SELECT` · `LOCATION_SELECT` · `COUNTRY_SELECT` ·
`COUNTRY_MULTI_SELECT` · `LOCALE_SELECT` · `LOCALE_MULTI_SELECT` · `TIME_ZONE_SELECT` ·
`HTML_EDITOR` · `WYSIWYG` · `CAPTCHA` · `OTP` · `SSOTP`

**View fields** (`ViewFieldType`) — display only:

`READONLY` · `MESSAGE` · `DIVIDER` · `HTML_VIEW` · `IMAGE_VIEW` · `AVATAR_VIEW` · `GRAVATAR` ·
`FILE_DOWNLOAD` · `PAYMENT`

**Filter fields** (`FilterFieldType`) — produce a `Filter`, for search forms:

`TEXT` · `LONG` · `DATE` · `DATE_TIME` · `TIME` · `IP4` · `IP6` · `SINGLE_SELECT` · `MULTI_SELECT` ·
`SINGLE_SELECT_SEARCHABLE` · `MULTI_SELECT_SEARCHABLE`

Fields are serialised with `@JsonTypeInfo(use = MINIMAL_CLASS, property = "@type")`, which is how the
client picks a widget.

### Select options

Select fields take an `OptionDataSource` returning `SelectOption(id, title, description, icon, type,
disable)`:

| Implementation | Source |
| --- | --- |
| `ConstantOptionDataSource` | A fixed list defined in configuration. |
| `I18nConstantOptionDataSource` | Ids in configuration, titles from the `MessageSource`. |
| `EnumOptionDataSource` | A Java enum. |
| `PropertiesFileOptionDataSource` | A `.properties` file. |
| `NumberOptionDataSource` | A numeric range. |
| `BaseModelOptionDataSource` | A `bard-crud` service — options straight from the database. |
| `CachableOptionDataSource` | Decorator that caches any of the above. |

## Part 2 — Tables

`TableTemplate` is a `TableModel` plus the templates needed to build one:

```xml
<bean name="CustomerPage" class="org.bardframework.table.TableTemplate"
      p:name="customer" p:pageable="true" p:preload="true" p:delete="true"
      c:modelClass="com.example.CustomerModel"
      c:messageSource-ref="messageSource">

    <property name="filterFormTemplate"> ... </property>
    <property name="saveFormTemplate">   ... </property>
    <property name="updateFormTemplate"> ... </property>

    <constructor-arg name="headerTemplates">
        <util:list>
            <bean class="org.bardframework.table.header.StringHeaderTemplate"       p:name="email"/>
            <bean class="org.bardframework.table.header.LocalDateHeaderTemplate"    p:name="birthDate"/>
            <bean class="org.bardframework.table.header.EnumHeaderTemplate"         p:name="status"/>
        </util:list>
    </constructor-arg>
</bean>
```

One bean therefore describes an entire CRUD screen: the grid, its search form, its create dialog and
its edit dialog. Serve it with
[`TableModelRestController`](https://github.com/bardframework/bard-crud) and the client renders the
page with no page-specific code.

A `HeaderTemplate` reads the property off the model by name (reflection) and formats it. `format()`
is used for the JSON response and `formatForExport()` for Excel, so a date can be Jalali on screen
and a real date cell in the spreadsheet. Available templates cover strings, all numeric types,
booleans, enums (raw and i18n), `LocalDate`/`LocalDateTime`/`LocalTime`, epoch-millis dates,
durations, file sizes, IPv4/IPv6, images, avatars and nested property paths
(`StringPropertyPathHeaderTemplate`, e.g. `p:name="owner.company.title"`).

Header text follows the same i18n cascade: `header.<table>.<column>.title`,
`table.<name>.title`, `table.<name>.pageable`, …

## Part 3 — bard-flow

A flow is a **multi-step conversation whose state lives on the server**, addressed by a token the
client carries but cannot read.

This is the role Spring Web Flow played for session-bound applications, rebuilt for stateless ones:
the node keeps nothing, the conversation is externalised to Redis with a TTL, and the client is a
renderer rather than a participant in the state machine.

### The protocol

Four operations on one URL:

| Method | Headers / params | Meaning |
| --- | --- | --- |
| `GET /flow` | query params become the initial data | **start** — mint a token, run flow pre-processors, return the first form |
| `POST /flow` | `X-Flow-Token`, JSON body | **submit** — validate, persist into flow data, run post-processors, return the next form |
| `GET /flow` | `X-Flow-Token` | **getCurrent** — return the current form again (page reload, app resume) without re-running pre-processors |
| `PUT /flow?action=…` | `X-Flow-Token` | **action** — a side task that does *not* advance the flow (resend OTP, new captcha, download) |

Response:

```json
{
  "id": "aK39fjW2…",
  "form": { "name": "verify", "title": "…", "fields": [ … ] },
  "steps": 4,
  "current": 1,
  "finished": false,
  "fieldErrors": { "otp": "کد وارد شده صحیح نیست" },
  "errors": []
}
```

When `finished` is `true`, no token is returned and the conversation has been evicted.

### Wiring one up

```xml
<bean id="signupFlow" class="org.bardframework.flow.FlowHandlerImpl"
      c:name="signup"
      c:flowDataRepository-ref="flowDataRepository">
    <constructor-arg name="forms">
        <util:list>
            <bean class="org.bardframework.flow.form.FlowFormTemplate" c:name="mobile"
                  c:messageSource-ref="messageSource">
                <constructor-arg name="fieldTemplates">
                    <util:list>
                        <bean class="...CaptchaFieldTemplate" c:name="captcha"
                              c:otpGenerator-ref="captchaGenerator"
                              c:audioCaptchaGenerator-ref="audioCaptcha"/>
                        <bean class="...PhoneNumberFieldTemplate" c:name="mobile"/>
                    </util:list>
                </constructor-arg>
            </bean>

            <bean class="org.bardframework.flow.form.FlowFormTemplate" c:name="verify"
                  c:messageSource-ref="messageSource">
                <constructor-arg name="fieldTemplates">
                    <util:list>
                        <bean class="...SotpFieldTemplate" c:name="otp"
                              c:otpGenerator-ref="otpGenerator"
                              c:maxTryToResolveCount="3"
                              c:messageSender-ref="smsSender"/>
                    </util:list>
                </constructor-arg>
                <property name="postProcessors">
                    <util:list><bean class="com.example.CreateAccountProcessor"/></util:list>
                </property>
            </bean>
        </util:list>
    </constructor-arg>
</bean>
```

```java
@RestController
@RequestMapping("api/signup")
public class SignupController implements FlowController {
    private final FlowHandler flowHandler;
    SignupController(@Qualifier("signupFlow") FlowHandler flowHandler) { this.flowHandler = flowHandler; }
    @Override public FlowHandler getFlowHandler() { return flowHandler; }
}
```

Extend `FlowHandlerAbstract<D extends FlowData>` when you want a typed flow-data class or custom
error handling; `FlowHandlerImpl` rethrows and leaves it to your `@ControllerAdvice`
(`FlowExceptionControllerAdvice` is provided).

### How a step is chosen

```java
getForms().subList(currentIndex + 1, size).stream()
          .filter(form -> form.mustShow(flowData.getData()))
          .findFirst()
```

The next form is the first one *after the current index* whose `showExpression` holds against the
data gathered so far. A flow is therefore a **conditional graph**, not a fixed list: answer
"company" and the legal-entity form appears; answer "individual" and it is skipped. When nothing
matches, the flow is finished.

`currentFormIndex` only moves forward — there is no back step. This is a deliberate fit for the
one-way funnels flows are used for (signup, verification, recovery).

### Processors

`FormProcessor` is the extension point, and it is registered at three levels:

```
flow      preProcessors     run once at start
          postProcessors    run once when finished
          actionProcessors  action fallback

form      preProcessors     before the form is shown
          postProcessors    after the form is validated and persisted
          actionProcessors  actions for this form

field     actionProcessors  actions owned by one field (resend, audio captcha…)
```

An action is resolved **field → form → flow**; if nothing handles it, the flow is invalidated. Every
processor has `mustExecute(data)` (SpEL via `setExecuteExpression`) and `order()`.

Ready-made processors:

| Processor | Does |
| --- | --- |
| `DataProviderDatabaseProcessor` | Runs a query and injects the result into flow data. |
| `DataProviderHttpCallProcessor` | Calls a remote service and injects the response. |
| `DataProviderCsvFileProcessor` | Loads rows from a CSV. |
| `DataProviderHttpRequestHeader/Cookie/Parameter/PropertyProcessor` | Lifts values off the incoming request (client IP, user agent, a partner id in a cookie…). |
| `MessageSenderProcessor` | Renders a message (`MessageProviderMessageSource` / `MessageProviderResource`) and sends it via `MessageSenderSms` / `MessageSenderEmail` / `MessageSenderNoOp`. |

Because data providers write into the same map the forms read from, a value fetched in step 1 can
drive a `showExpression` in step 3 with no glue code.

### Self-contained OTP and captcha fields

The most useful part of `bard-flow`: a field template that carries its own protocol.

| Template | Channel | Notes |
| --- | --- | --- |
| `SotpFieldTemplate` | SMS | Sends via a `MessageSender`; resend action `sotp-resend`. |
| `EotpFieldTemplate` | Email | Same shape, over mail. |
| `TotpFieldTemplate` | Authenticator app | RFC 6238 time-based codes; also `HotpService` for counter-based. |
| `SsotpFieldTemplate` | **User-sent** SMS | Inverted flow: the *user* sends an SMS containing the code; `SsotpMessageReceiver` matches the inbound message against the flow. |
| `CaptchaFieldTemplate` | image (+ audio) | Emits a `data:image/png;base64,…` payload; optional audio captcha action for accessibility. |

Drop one into a form and you get, without writing any of it:

* **generation and delivery at the right moment** — `preProcess` fires when the form becomes current,
  not when the flow starts;
* **resend with a cooldown** — `425 Too Early` plus the remaining seconds in the body, and a hard
  `maxSendCount` ceiling;
* **brute-force protection** — `maxTryToResolveCount`; exceeding it throws `InvalidateFlowException`
  and destroys the conversation;
* **counters kept inside the conversation** (`X_RESOLVE_COUNT`, `X_SENT_TIME`, `X_GENERATE_COUNT`),
  cleared on success — invisible and unreachable from the client;
* **`persistentValue = false`** — the code and the captcha answer are never written into the
  submitted flow data.

Configure the code itself with an `OtpGenerator`: `OtpGeneratorRegex` (a regex describing the shape,
e.g. `[0-9]{6}`) or `OtpGeneratorFixed` (a constant — for tests only).

### Storing conversations

```java
@Bean
public FlowDataRepository<FlowData> flowDataRepository(RedisTemplate<String, Object> template) {
    return new FlowDataRepositoryRedis<>(template, Duration.ofMinutes(15).toMillis());
}
```

`FlowDataRepositoryInMemory` exists for tests and single-node development. Implement
`FlowDataRepository` yourself for any other backing store.

### Security notes

* **State never leaves the server.** The client holds an opaque random token (10–50 alphanumerics by
  default; override `generateFlowToken`). It cannot skip a step, replay an OTP or reset a counter.
* **Flow data is saved in a `finally` block.** If an exception occurs *after* a captcha was consumed,
  the consumption is still recorded — otherwise a failed request would hand back a reusable captcha.
* **`InvalidateFlowException` destroys the conversation** rather than returning an error, for
  anything that looks like abuse.
* **Unknown fields are rejected**, and disabled fields are not accepted even if the client sends them.

## i18n key reference

| Key pattern | Applies to |
| --- | --- |
| `form.<form>.<property>` | title, description, submitLabel, confirmMessage, submitEmptyInputs, autoSubmitDelaySeconds, nestedFormShowType… |
| `field.<form>.<field>.<property>` | title, description, info, placeholder, errorMessage, required, disable, and type-specific ones (maxLength, minValue…) |
| `field.<field>.<property>` | same, applied to that field name in any form |
| `table.<table>.<property>` | title, description, pageable, preload, export, print, delete, hideColumn… |
| `header.<table>.<column>.<property>` | title, description, hidden, sortable, movable, sticky |

## Clients

* **Angular** — [`@bard/angular`](https://github.com/bardframework/bard-angular): `<bard-form>`,
  `<bard-table>`, `<bard-flow [apiUrl]="…">`.
* **Android** — [bard-android](https://github.com/bardframework/bard-android): `FormFragment`,
  `FormsFragment`, one `ViewHolder` per field type.

## License

Apache License 2.0.
