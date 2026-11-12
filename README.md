# SOC Event Simulation Dashboard

An experimental dashboard for visualising synthetic security events and exploring a Java/Spring Boot streaming architecture using Server-Sent Events (SSE).

<p align="center">
  <a href="https://tzayadet.github.io/CyberOps-Monitor/"><img src="https://img.shields.io/badge/LIVE_DEMO-OPEN-00a86b?style=flat-square" alt="Live demo"></a>
  <a href="https://github.com/tzayadet/CyberOps-Monitor"><img src="https://img.shields.io/badge/SOURCE-GITHUB-161b22?style=flat-square&logo=github" alt="Source"></a>
</p>

[![Java CI](https://github.com/tzayadet/CyberOps-Monitor/actions/workflows/java-ci.yml/badge.svg)](https://github.com/tzayadet/CyberOps-Monitor/actions/workflows/java-ci.yml) · [![Pages](https://github.com/tzayadet/CyberOps-Monitor/actions/workflows/jekyll-gh-pages.yml/badge.svg)](https://github.com/tzayadet/CyberOps-Monitor/actions/workflows/jekyll-gh-pages.yml)

> All events, countries, alerts and severity levels shown in the demo are synthetic. They are not live telemetry or threat intelligence.

## What it does

- Displays a visual event stream and simulated alerts.
- Provides a browser interface for exploring dashboard components.
- Includes a separate Java backend with an SSE endpoint.
- Demonstrates asynchronous event delivery using Server-Sent Events.

## Technology

- Java 17
- Spring Boot 3.2
- Spring Web and Server-Sent Events
- HTML, CSS and JavaScript

## Run the backend

**Requirements:** JDK 17 and Maven.

```bash
mvn spring-boot:run
```

The backend listens on `127.0.0.1:8080` by default. To serve the static interface locally:

```bash
python -m http.server 5500
```

The published browser demo currently runs its own simulation and does not consume the Java SSE endpoint. The backend is a separate component.

## Project structure

| Path | Purpose |
|---|---|
| `index.html` | Static dashboard |
| `src/main/java/` | Spring Boot backend |
| `src/test/java/` | Backend tests |
| `pom.xml` | Maven configuration |
| `.github/workflows/` | CI and Pages workflows |

## Tests

```bash
mvn --batch-mode verify
```

## Scope and limitations

This is not a SIEM, SOC or detection platform. It does not connect to EDR, SIEM, CTI feeds or production infrastructure. Events are generated for demonstration.

The GitHub Pages site publishes only the static interface; it does not run the Java backend. Do not expose the backend publicly without appropriate authentication, authorization, restrictive CORS and resource controls.

## Deployment

The static interface is published through GitHub Pages. Backend deployment requires a separate Java hosting environment.

## License

MIT. See [LICENSE](LICENSE).
