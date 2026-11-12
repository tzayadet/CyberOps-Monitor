# SOC Event Simulation Dashboard

An interactive SOC-style dashboard for exploring synthetic security events, plus a separate Java/Spring Boot backend that streams generated events through Server-Sent Events (SSE).

<p align="center">
  <a href="https://tzayadet.github.io/CyberOps-Monitor/"><img src="https://img.shields.io/badge/LIVE_DEMO-OPEN-00a86b?style=flat-square" alt="Live demo"></a>
  <a href="https://github.com/tzayadet/CyberOps-Monitor"><img src="https://img.shields.io/badge/SOURCE-GITHUB-161b22?style=flat-square&logo=github" alt="Source"></a>
</p>

[![Java CI](https://github.com/tzayadet/CyberOps-Monitor/actions/workflows/java-ci.yml/badge.svg)](https://github.com/tzayadet/CyberOps-Monitor/actions/workflows/java-ci.yml) · [![Pages](https://github.com/tzayadet/CyberOps-Monitor/actions/workflows/jekyll-gh-pages.yml/badge.svg)](https://github.com/tzayadet/CyberOps-Monitor/actions/workflows/jekyll-gh-pages.yml)

> **Simulation only:** events, actors, countries, alerts and severity levels are synthetic. The dashboard is not connected to live telemetry, threat intelligence, EDR or SIEM platforms.

## Features

- Browser dashboard with a simulated event stream and alert feed.
- Separate Spring Boot API for generating SSE events.
- Short-lived opaque session IDs issued by `/api/connect`.
- SSE stream access restricted to registered, unexpired session IDs.
- Session registry capped atomically at 500 entries, including concurrent connection attempts; sessions expire after 10 minutes.
- Maximum of 20 active SSE streams, with a 60-second stream timeout.
- CORS allowlist configurable through `app.cors.allowed-origins`.

## Technology

- Java 17
- Spring Boot
- Spring Web and Server-Sent Events
- HTML, CSS and JavaScript
- Maven

## Run the backend locally

**Requirements:** JDK 17 and Maven.

```bash
mvn spring-boot:run
```

The backend listens on `127.0.0.1:8080` by default.

Request a session:

```bash
curl http://127.0.0.1:8080/api/connect
```

Use the returned `sessionId` to open its event stream:

```bash
curl -N http://127.0.0.1:8080/api/stream/SESSION_ID
```

To serve the static interface locally:

```bash
python -m http.server 5500
```

The published GitHub Pages dashboard runs its own browser-side simulation and does not consume the Java SSE endpoint. The backend is a separate local component.

## Project structure

| Path | Purpose |
|---|---|
| `index.html` | Static dashboard |
| `src/main/java/` | Spring Boot API and SSE event generator |
| `src/test/java/` | Backend tests |
| `pom.xml` | Maven configuration |
| `.github/workflows/` | CI and Pages workflows |

## Tests

```bash
mvn --batch-mode verify
```

The test suite covers session issuance, UUID uniqueness, concurrent session-cap enforcement, CORS restrictions, malformed stream IDs and rejection of unknown session IDs.

## Scope and limitations

This is an educational prototype, not a SIEM, SOC, detection engine or production threat-monitoring service. It does not ingest or analyse real telemetry and must not be used to make incident-response decisions.

Session IDs are short-lived bearer capabilities, not user authentication. There is no user identity, role-based authorization, durable event storage or production audit trail. Keep the backend on loopback unless you add a proper authentication and deployment model. CORS is not an authentication control.

## Deployment

GitHub Pages publishes only the static interface. The Java backend is not deployed and requires a separate Java hosting environment if you choose to run it elsewhere.

## License

MIT. See [LICENSE](LICENSE).
