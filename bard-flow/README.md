bard-flow
=========

[![Maven Central](https://img.shields.io/badge/maven--central-6.1.4-blue.svg)](https://repo1.maven.org/maven2/org/bardframework/form/bard-flow/)
[![License](http://img.shields.io/:license-apache-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

**A flow engine for stateless applications** — multi-step conversations whose state lives on the
server, addressed by a token the client carries but cannot read.

This is the role Spring Web Flow played for session-bound web applications, rebuilt for REST
backends, SPAs and mobile clients: the node keeps nothing, the conversation is externalised, and the
client renders rather than decides.

Part of [**Bard Form &amp; Flow**](../README.md) · [Bard Framework](https://github.com/bardframework)

```xml
<dependency>
    <groupId>org.bardframework.form</groupId>
    <artifactId>bard-flow</artifactId>
    <version>6.1.4</version>
</dependency>
```

## The problem it solves

Signup, password reset, KYC, checkout — all are conversations: several screens, order that depends on
earlier answers, side effects (send an SMS, call a bureau) between steps, and steps that must not be
skipped.

The two usual approaches both fail:

* **`HttpSession`** (Spring Web Flow) — needs sticky sessions, scales badly, and every rolling deploy
  kills in-flight conversations.
* **State in the client** (a wizard in the SPA, a JWT carrying progress) — the user controls it, so
  "you have 3 attempts left" and "you already passed the captcha" are suggestions rather than facts.

Bard Flow puts the state in an external store keyed by an opaque token. **The node is stateless; the
conversation is stateful.** The client holds a random string and nothing else.

## Protocol

| Method | Headers / params | Meaning |
| --- | --- | --- |
| `GET /flow` | query params become initial data | **start** — mint a token, run flow pre-processors, return the first form |
| `POST /flow` | `X-Flow-Token`, JSON body | **submit** — validate, persist, run post-processors, return the next form |
| `GET /flow` | `X-Flow-Token` | **getCurrent** — re-return the current form (page reload, app resume); pre-processors are *not* re-run |
| `PUT /flow?action=…` | `X-Flow-Token` | **action** — a side task that does not advance the flow |

```json
{
  "id": "aK39fjW2xQ",
  "form": { "name": "verify", "fields": [ … ] },
  "steps": 3, "current": 1,
  "finished": false,
  "fieldErrors": { "otp": "کد صحیح نیست" },
  "errors": []
}
```

`finished: true` means the conversation is over and has been evicted; no token is returned.

## Setting one up

```xml
<bean id="signupFlow" class="org.bardframework.flow.FlowHandlerImpl"
      c:name="signup" c:flowDataRepository-ref="flowDataRepository">
    <constructor-arg name="forms">
        <util:list>

            <bean class="org.bardframework.flow.form.FlowFormTemplate" c:name="mobile"
                  c:messageSource-ref="messageSource">
                <constructor-arg name="fieldTemplates">
                    <util:list>
                        <bean class="org.bardframework.flow.form.field.input.captcha.CaptchaFieldTemplate"
                              c:name="captcha"
                              c:otpGenerator-ref="captchaGenerator"
                              c:audioCaptchaGenerator-ref="audioCaptchaGenerator"/>
                        <bean class="org.bardframework.form.field.input.PhoneNumberFieldTemplate" c:name="mobile"/>
                    </util:list>
                </constructor-arg>
            </bean>

            <bean class="org.bardframework.flow.form.FlowFormTemplate" c:name="verify"
                  c:messageSource-ref="messageSource">
                <constructor-arg name="fieldTemplates">
                    <util:list>
                        <bean class="org.bardframework.flow.form.field.input.otp.sms.SotpFieldTemplate"
                              c:name="otp"
                              c:otpGenerator-ref="otpGenerator"
                              c:maxTryToResolveCount="3"
                              c:messageSender-ref="smsMessageSender"/>
                    </util:list>
                </constructor-arg>
                <property name="postProcessors">
                    <util:list><bean class="com.example.CreateAccountProcessor"/></util:list>
                </property>
            </bean>

            <bean class="org.bardframework.flow.form.FlowFormTemplate" c:name="done"
                  c:messageSource-ref="messageSource" p:finished="true">
                <constructor-arg name="fieldTemplates">
                    <util:list>
                        <bean class="org.bardframework.form.field.view.MessageFieldTemplate" c:name="welcome"/>
                    </util:list>
                </constructor-arg>
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

`FlowController` is an interface with default methods — implementing it is the whole HTTP layer.

## Choosing the next step

```java
getForms().subList(currentIndex + 1, size).stream()
          .filter(form -> form.mustShow(flowData.getData()))
          .findFirst()
```

The next form is the first one after the current index whose `showExpression` holds against the data
gathered so far. A flow is a **conditional graph**, not a fixed list. When nothing matches, the flow
finishes.

`currentFormIndex` only advances — there is no back step, which fits the one-way funnels flows are
used for.

## Processors

`FormProcessor` is the extension point, registered at three levels and consulted in this order for an
action:

```
field   actionProcessors    ← tried first
form    preProcessors / postProcessors / actionProcessors
flow    preProcessors / postProcessors / actionProcessors   ← tried last
```

An action nothing handles invalidates the flow. Every processor has `mustExecute(data)` (SpEL, via
`setExecuteExpression`) and `order()`.

| Built-in processor | Does |
| --- | --- |
| `DataProviderDatabaseProcessor` | Runs a query, injects the result into flow data. |
| `DataProviderHttpCallProcessor` | Calls a remote service, injects the response. |
| `DataProviderCsvFileProcessor` | Loads rows from a CSV. |
| `DataProviderHttpRequestHeaderProcessor` | Lifts a request header (client IP, user agent) into flow data. |
| `DataProviderHttpRequestCookieProcessor` | The same for a cookie. |
| `DataProviderHttpRequestParameterProcessor` | The same for a query parameter. |
| `DataProviderHttpRequestPropertyProcessor` | The same for a request property. |
| `MessageSenderProcessor` | Renders a message and sends it. |

Message rendering uses `MessageProviderMessageSource` (i18n keys) or `MessageProviderResource` (a
template file); delivery uses `MessageSenderSms`, `MessageSenderEmail` or `MessageSenderNoOp`.

Because providers write into the same map forms read from, a value fetched in step 1 can drive a
`showExpression` in step 3 with no glue code.

## Self-contained OTP and captcha fields

| Template | Channel | Notes |
| --- | --- | --- |
| `SotpFieldTemplate` | SMS | Resend action `sotp-resend`. |
| `EotpFieldTemplate` | Email | Same shape over mail. |
| `TotpFieldTemplate` | Authenticator app | RFC 6238; `HotpService` for counter-based. |
| `SsotpFieldTemplate` | **user-sent** SMS | The *user* sends an SMS containing the code; `SsotpMessageReceiver` matches the inbound message to the conversation. |
| `CaptchaFieldTemplate` | image (+ audio) | Emits `data:image/png;base64,…`; `audio` action for accessibility. |

Adding one gives you, without writing any of it:

* **delivery at the right moment** — `preProcess` fires when that form becomes current, not at start;
* **resend with cooldown** — `425 Too Early` plus the remaining seconds, and a `maxSendCount` ceiling;
* **brute-force protection** — `maxTryToResolveCount`, then `InvalidateFlowException` destroys the
  conversation;
* **counters inside the conversation** (`X_RESOLVE_COUNT`, `X_SENT_TIME`, `X_GENERATE_COUNT`), cleared
  on success and unreachable from the client;
* **`persistentValue = false`** — the code and the captcha answer never enter the submitted flow data;
* **`validationOrder`** — the captcha is validated before every other field.

Codes are produced by an `OtpGenerator`: `OtpGeneratorRegex` (a pattern such as `[0-9]{6}`) or
`OtpGeneratorFixed` (a constant — tests only).

## Storage

```java
@Bean
public FlowDataRepository<FlowData> flowDataRepository(RedisTemplate<String, Object> template) {
    return new FlowDataRepositoryRedis<>(template, Duration.ofMinutes(15).toMillis());  // bard-flow-redis
}
```

`FlowDataRepositoryInMemory` covers tests and single-node development. Implement
`FlowDataRepository` for anything else.

## Exceptions

| Exception | Meaning |
| --- | --- |
| `FlowDataValidationException` | Submitted data is invalid; optionally re-renders the current form. |
| `FormDataValidationException` | Field-level errors from `bard-form`. |
| `FlowExecutionException` | A processor failed with a message for the user. |
| `InvalidateFlowException` | Abuse or an unusable state — the conversation is destroyed. |
| `MaxOtpSendExceededException` | The send ceiling was reached. |

Implement `FlowExceptionControllerAdvice` in your `@RestControllerAdvice` to map them to responses.
Extend `FlowHandlerAbstract<D extends FlowData>` for a typed flow-data class or custom handling;
`FlowHandlerImpl` simply rethrows.

## Security notes

* **State never leaves the server.** The token is 10–50 random alphanumerics (override
  `generateFlowToken`). A client cannot skip a step, replay an OTP or reset a counter.
* **Flow data is written in a `finally` block.** If a request fails *after* a captcha was consumed,
  the consumption is still recorded — otherwise a failed request would return a reusable captcha.
* **Unknown and disabled fields are rejected** on submit, not ignored.
* **`InvalidateFlowException` destroys the conversation** rather than returning an error.

Pair with [`common-waf`](https://github.com/bardframework/bard-commons) to rate-limit the `start`
endpoint: the flow protects what happens inside a conversation, the WAF protects how many
conversations one caller may open.

## License

Apache License 2.0.
