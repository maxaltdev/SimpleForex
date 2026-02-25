## About

**SimpleForex** is a small Java library to model and fetch basic [forex market](https://en.wikipedia.org/wiki/Foreign_exchange_market) data.
It provides model classes for basic forex concepts and easy ways to fetch data from exchange rate web APIs.

Goals:
* A "turnkey" solution to accessing exchange rate data in Java with minimal fuss
* Convention over configuration - both in Java and forex concepts
* KISS
* Modernity (latest Java features, best practices, etc.)
* Primarily functional (Java 8 style) API (`Stream`, `Optional`, value-based classes, etc.)
* Minimal amount of 3rd party dependencies

Non-goals:
* Arithmetic flawlessness (such as complete prevention of cumulative error in chained arithmetic operations)
* Compatibility with legacy conventions (such as JavaBeans)

> [!WARNING]
> If you're using this library in mission-critical financial applications, **do so at your own discretion.**

## Usage examples

*coming soon*

## Installation

Maven coordinates:

```xml
<dependency>
    <groupId>dev.maxalt</groupId>
    <artifactId>simpleforex</artifactId>
    <version>0.3.0</version>
</dependency>
```

## License

This code is available under a [3-clause BSD license](LICENSE).
