<p align="center">
  <img src="https://img.shields.io/badge/BLUE_TEAM-SOC_EVENT_SIMULATOR-101010?style=for-the-badge&labelColor=050805&color=00FF41" alt="SOC Event Simulator"/>
  <img src="https://img.shields.io/badge/JAVA-SPRING_BOOT-101010?style=for-the-badge&logo=springboot&logoColor=00FF41" alt="Spring Boot"/>
</p>

# SOC Event Simulation Dashboard

Dashboard experimental para visualizar eventos de seguridad sintéticos y explorar una arquitectura de streaming basada en Java, Spring Boot y Server-Sent Events (SSE).

<p align="center">
  <a href="https://tzayadet.github.io/CyberOps-Monitor/"><strong>LIVE DEMO ↗</strong></a> ·
  <a href="https://github.com/tzayadet/CyberOps-Monitor"><strong>SOURCE CODE</strong></a>
</p>

[![Java CI](https://github.com/tzayadet/CyberOps-Monitor/actions/workflows/java-ci.yml/badge.svg)](https://github.com/tzayadet/CyberOps-Monitor/actions/workflows/java-ci.yml) · [![GitHub Pages](https://github.com/tzayadet/CyberOps-Monitor/actions/workflows/jekyll-gh-pages.yml/badge.svg)](https://github.com/tzayadet/CyberOps-Monitor/actions/workflows/jekyll-gh-pages.yml)

> Los eventos, países, alertas y severidades de la demo son ficticios. No representan telemetría, detecciones ni inteligencia de amenazas reales.

## What it does

- Presenta un mapa visual y un flujo de eventos sintéticos.
- Simula alertas y actividad para probar componentes de visualización.
- Incluye un backend Java con un endpoint SSE independiente.
- Permite explorar actualizaciones asíncronas mediante Server-Sent Events.

## Technology

- Java 17
- Spring Boot 3.2
- Spring Web
- Server-Sent Events
- HTML, CSS y JavaScript

## Run the backend

**Requirements:** JDK 17 and Maven.

```bash
mvn spring-boot:run
```

El backend escucha por defecto en `127.0.0.1:8080`.

Para servir la interfaz estática en local:

```bash
python -m http.server 5500
```

La interfaz publicada actualmente ejecuta su propia simulación en el navegador y no consume el endpoint SSE. El backend es un componente separado.

## Tests

```bash
mvn --batch-mode verify
```

GitHub Actions ejecuta la compilación y las pruebas en push y pull requests.

## Project layout

| Path | Purpose |
|---|---|
| `index.html` | Dashboard estático |
| `src/main/java/` | Backend Spring Boot y configuración CORS |
| `src/test/java/` | Pruebas del backend |
| `pom.xml` | Configuración Maven |
| `.github/workflows/` | CI y publicación |

## Scope & limitations

No es un SIEM, SOC ni plataforma de detección. No se conecta a EDR, SIEM, feeds CTI ni infraestructura real. Los eventos son generados para demostración.

El backend no debe exponerse a redes públicas sin autenticación, autorización, CORS restrictivo y límites de recursos.

## Deployment

GitHub Pages publica únicamente la interfaz estática. No ejecuta el backend Java. La demo no debe confundirse con una consola SOC conectada a fuentes reales.

## License

MIT. See [LICENSE](LICENSE).
