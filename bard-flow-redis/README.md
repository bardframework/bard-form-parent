bard-flow-redis
===============

[![Maven Central](https://img.shields.io/badge/maven--central-5.3.4-blue.svg)](https://repo1.maven.org/maven2/org/bardframework/form/bard-flow-redis/)
[![License](http://img.shields.io/:license-apache-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

Redis storage for [`bard-flow`](../bard-flow) conversations — the piece that makes a stateful
conversation work on stateless nodes.

Part of [**Bard Form &amp; Flow**](../README.md) · [Bard Framework](https://github.com/bardframework)

```xml
<dependency>
    <groupId>org.bardframework.form</groupId>
    <artifactId>bard-flow-redis</artifactId>
    <version>5.3.4</version>
</dependency>
```

## Usage

```java
@Bean
public FlowDataRepository<FlowData> flowDataRepository(RedisTemplate<String, Object> redisTemplate) {
    return new FlowDataRepositoryRedis<>(redisTemplate, Duration.ofMinutes(15).toMillis());
}
```

For a typed flow-data class, bind the type parameter:

```java
public class SignupFlowData extends FlowData { … }

@Bean
public FlowDataRepository<SignupFlowData> flowDataRepository(RedisTemplate<String, Object> template) {
    return new FlowDataRepositoryRedis<SignupFlowData>(template, 900_000) { };
}
```

The anonymous subclass is what lets `getGenericArgType` discover the concrete type for
deserialisation; without it the repository falls back to plain `FlowData`.

## Behaviour

* Data is stored as JSON via `DataManagerRedisImpl`
  ([`common-redis`](https://github.com/bardframework/bard-commons)), so entries are readable when you
  are debugging a stuck conversation.
* **The TTL is applied on every write.** Each interaction refreshes the window, so the expiry is an
  inactivity timeout rather than a hard deadline.
* A token with no data throws `InvalidateFlowException` — an expired conversation and a forged token
  are indistinguishable to the client, which is the correct answer to both.

## Choosing the TTL

Long enough for a real user to receive an SMS, switch apps and come back — 10 to 15 minutes is
typical. Short enough that abandoned conversations do not accumulate. Since every step refreshes it,
a slow but active user is never cut off mid-flow.

## Why not in-memory

`FlowDataRepositoryInMemory` is fine for tests and one node. With two nodes, a user who starts a
conversation on node A and submits to node B gets `InvalidateFlowException` and has to start over.
Redis removes that, and conversations also survive a restart or a rolling deploy.

## License

Apache License 2.0.
