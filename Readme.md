# CyberOps Monitor

Dashboard visual de simulación de actividad SOC/Threat Intelligence, construido con HTML, CSS, JavaScript y un backend Java/Spring Boot con Server-Sent Events (SSE).

## Qué hace

- Presenta un mapa visual y un flujo de eventos sintéticos.
- Genera mensajes y alertas ficticias desde el backend.
- Expone un canal SSE para alimentar la interfaz en tiempo real.
- Sirve como demostración de visualización y eventos asíncronos.

## Qué no hace

Los países, ataques, bots, severidades y estados se generan aleatoriamente. **No son telemetría real, inteligencia de amenazas ni detecciones de seguridad.** El proyecto no se conecta a un SIEM, EDR, fuentes CTI ni infraestructura de producción.

## Stack

- Java 17
- Spring Boot 3.2
- Spring Web
- HTML, CSS y JavaScript

## Ejecución del backend

Requisitos: JDK 17 y Maven.

```bash
mvn spring-boot:run
```

El backend se inicia en el puerto 8080 por defecto. La interfaz estática puede abrirse localmente con:

```bash
python -m http.server 5500
```

La UI estática y el backend deben servirse desde orígenes compatibles con la configuración de CORS. No expongas el backend en una red pública sin autenticación, autorización, CORS restrictivo y límites de recursos.

## GitHub Pages

GitHub Pages puede publicar la interfaz estática, pero no ejecuta el backend Java. La página desplegada no debe interpretarse como una conexión a un SOC real.

## Licencia

MIT. Consulta [LICENSE](LICENSE).
