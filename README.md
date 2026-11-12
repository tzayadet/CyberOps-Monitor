# CyberOps Monitor

SOC / Threat Intelligence dashboard simulator built with HTML, CSS, JavaScript and a Java / Spring Boot backend using Server-Sent Events (SSE).

## What it does

- Displays a visual map and a stream of synthetic events.
- Generates fictional messages and alerts from the backend.
- Exposes an SSE endpoint to feed the interface with real-time events.
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

The backend listens on port 8080 by default. Serve the static interface locally with:

```bash
python -m http.server 5500
```

The static UI and backend must use compatible origins and CORS configuration. Do not expose the backend to a public network without authentication, authorization, restrictive CORS rules and resource limits.

## GitHub Pages

GitHub Pages can publish the static interface, but it cannot execute the Java backend. The public page is therefore a frontend demonstration, not a connection to a live SOC.

## Source files

- `index.html`: static dashboard interface.
- `CyberOpsBackend.java`: Java backend.
- `pom.xml`: Maven configuration.
- `Readme.md`: earlier project notes.

## License

MIT. See [LICENSE](LICENSE).
