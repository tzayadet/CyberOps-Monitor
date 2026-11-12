# SOC Event Simulation Dashboard

**Repository:** CyberOps-Monitor

SOC event and threat-visualization prototype built with HTML, CSS, JavaScript and a separately runnable Java / Spring Boot backend exposing a Server-Sent Events (SSE) endpoint.

**Static demo:** https://tzayadet.github.io/CyberOps-Monitor/  
The Pages version is a self-contained browser-side simulation. The Java/Spring Boot backend exposes a separate SSE endpoint, but the current frontend does not consume it. Generated events are synthetic, not real telemetry.

## What it does

- Displays a visual map and a stream of synthetic events.
- Generates fictional messages and alerts from the backend.
- Provides a separate SSE endpoint that streams synthetic events to an SSE client.
- Demonstrates event-driven UI updates and asynchronous backend communication.

## What it does not do

Countries, attacks, bots, severity levels and statuses are generated randomly. **They are not real telemetry, threat intelligence or security detections.** The project does not connect to a SIEM, EDR, CTI feeds or production infrastructure.

## Technology stack

- Java 17
- Spring Boot 3.2
- Spring Web
- HTML, CSS and JavaScript
- Server-Sent Events

## Run the backend locally

Requirements: JDK 17 and Maven.

```bash
mvn spring-boot:run
```

The backend listens on `127.0.0.1:8080` by default. To change the allowed browser origin, set `CORS_ALLOWED_ORIGINS` (comma-separated); to change the bind address, set `SERVER_ADDRESS`. Serve the static interface locally with:

```bash
python -m http.server 5500
```

The static UI currently runs its own simulation and does not call the backend. The backend allows only `http://localhost:5500` by default for future/local browser clients; configure `CORS_ALLOWED_ORIGINS` explicitly for another origin. Do not expose the backend to a public network without authentication, authorization, restrictive CORS rules and resource limits.

Run the backend tests with:

```bash
mvn --batch-mode verify
```

GitHub Actions performs this build and test on each push and pull request.

## GitHub Pages

GitHub Pages can publish the static interface, but it cannot execute the Java backend. The public page is therefore a frontend demonstration, not a connection to a live SOC.

## Source files

- `index.html`: static dashboard interface.
- `src/main/java/com/zyanetralys/cyberops/CyberOpsBackend.java`: Java/Spring Boot SSE backend.
- `src/main/java/com/zyanetralys/cyberops/CorsConfig.java`: explicit CORS allowlist.
- `src/main/resources/application.properties`: local bind address and CORS configuration.
- `src/test/java/com/zyanetralys/cyberops/CyberOpsBackendTest.java`: backend smoke test.
- `pom.xml`: Maven configuration.
- `Readme.md`: earlier project notes.

## License

MIT. See [LICENSE](LICENSE).
