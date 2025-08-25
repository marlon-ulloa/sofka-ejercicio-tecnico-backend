// DESCRIPCION DE METRICAS USADAS
// http_req_duration: Tiempo total de una solicitud HTTP hasta recibir completamente la respuesta.
// http_req_failed: Proporción de solicitudes HTTP fallidas respecto al total.
// http_req_waiting: Tiempo entre el envío de la solicitud y recepción del primer byte (TTFB).
// http_req_blocked: Tiempo en procesar la solicitud debido a DNS lookup o bloqueos.
// http_req_connecting: Tiempo para establecer la conexión TCP con el servidor.
// http_req_receiving: Tiempo en recibir el cuerpo de la respuesta HTTP.
// http_req_tls_handshaking: Tiempo para completar el handshake TLS/SSL.
// http_req_sending: Tiempo en enviar los datos de la solicitud al servidor.
// waiting_time: Tiempo promedio de espera del cliente por la respuesta del servidor.
// Errors: Número total de errores ocurridos durante la prueba.
// iterations: Total de iteraciones completas ejecutadas en la prueba.
// active_vus: Promedio de usuarios virtuales activos durante la prueba.
// transactions_per_second: Promedio de transacciones procesadas por segundo.
// vus_max: Máximo número de usuarios virtuales alcanzado en la prueba.
// retries: Total de reintentos por fallos en solicitudes.
// checks: Proporción de validaciones exitosas realizadas durante la prueba.

export default {
  endpoints: [
    {
        url: "http://localhost:8080/api/clientes",
        payload: "",
        method: "get",
        loadtest: {
          stages: [
            {duration: "30s",target: 50},
            {duration: "2m",target: 50},
            {duration: "30s",target: 0}
          ],
          thresholds: {
            http_req_duration: ["p(95) < 2000"], // Tiempo total de una solicitud HTTP hasta recibir completamente la respuesta.
            http_req_failed: ["rate < 0.01"], // Proporción de solicitudes HTTP fallidas respecto al total.
            waiting_time: ["avg < 1500"], // Tiempo promedio de espera del cliente por la respuesta del servidor.
            Errors: ["count < 5"], // Número total de errores ocurridos durante la prueba.
            iterations: ["count > 3000"], // Total de iteraciones completas ejecutadas en la prueba.
            http_req_waiting: ["p(95) < 500"], // Tiempo entre el envío de la solicitud y recepción del primer byte (TTFB).
            http_req_blocked: ["p(95) < 300"], // Tiempo en procesar la solicitud debido a DNS lookup o bloqueos.
            active_vus: ["avg > 20"], // Promedio de usuarios virtuales activos durante la prueba.
            http_req_connecting: ["p(95) < 200"], // Tiempo para establecer la conexión TCP con el servidor.
            transactions_per_second: ["avg >= 50"] // Promedio de transacciones procesadas por segundo.
          }
        },
        stresstest: {
          stages: [
            {duration: "30s", target: 200},
            {duration: "1m", target: 50},
            {duration: "1m", target: 75},
            {duration: "30s", target: 0}
          ],
          thresholds: {
            http_req_duration: ["p(95) < 5000"],
            waiting_time: ["avg < 3500"],
            Errors: ["count < 20"],
            http_req_failed: ["rate < 0.01"],
            http_req_connecting: ["p(95) < 1000"],
            http_req_blocked: ["p(95) < 500"],
            iterations: ["count > 10000"],
            vus_max: ["value >= 0"],
            http_req_waiting: ["p(99) < 10000"],
            retries: ["count < 10"]
          }
        },
        peaktest: {
          stages: [
            {duration: "1m", target: 5},
            {duration: "30s", target: 300},
            {duration: "1m", target: 10},
            {duration: "30s", target: 0}
          ],
          thresholds: {
            http_req_duration: ["p(95) < 3000"],
            waiting_time: ["avg < 2000"],
            Errors: ["count < 10"],
            http_req_failed: ["rate < 0.01"],
            http_req_receiving: ["p(95) < 1000"],
            http_req_tls_handshaking: ["p(95) < 500"],
            http_req_blocked: ["p(95) < 300"],
            vus_max: ["value >= 0"],
            iterations: ["count > 3000"],
            checks: ["rate > 0.95"],
            http_req_waiting: ["p(95) < 3000"]
          }
        },
        soakingtest: {
          stages: [
            {duration: "30s", target: 200},
            {duration: "2m", target: 200},//3h
            {duration: "30s", target: 0},
          ],
          thresholds: {
            http_req_duration: ["p(95) < 2000"],
            waiting_time: ["avg < 1200"],
            Errors: ["count <= 2"],
            http_req_failed: ["rate < 0.01"],
            http_req_sending: ["p(95) < 500"],
            transaction_duration: ['avg<2'],           // Duración media de transacciones
            transactions_per_second: ['avg>=20'],      // Transacciones por segundo
            iterations: ["count > 10000"],
            checks: ["rate > 0.99"]
          }
        },
        capacitytest: {
          stages: [
            { duration: '30s', target: 10 }, // Inicio gradual con 50 usuarios
            { duration: '1m', target: 40 }, // Incremento a 200 usuarios
            { duration: '1m', target: 30 }, // Incremento máximo a 400 usuarios
            { duration: '30s', target: 0 },  // Reducción a 0 usuarios
          ],
          thresholds: {
            http_req_duration: ['p(95)<3000'], // Respuesta aceptable en 95% de las solicitudes
            waiting_time: ["avg < 1200"],
            Errors: ["count < 2"],
            http_req_failed: ['rate<0.05'],    // Máximo 5% de fallos permitidos
          },
        },
        stabilitytest: {
          stages: [
           { duration: '30s', target: 25 }, // Ramp-up inicial
           { duration: '2m', target: 50 }, // Mantén 100 usuarios durante 1 hora
           { duration: '30s', target: 0 },   // Ramp-down al final
          ],
          thresholds: {
            http_req_duration: ['avg<2000', 'p(95)<2500'],
            waiting_time: ["avg < 1200"],
            Errors: ["count < 2"],
            http_req_failed: ['rate<0.01'],
          },
        }
    },
    {
       url: "http://localhost:8081/api/reportes?clienteId=0105131221&fechaInicio=2025-08-08T00:00:00&fechaFin=2025-08-31T23:59:59",
        payload: "",
        method: "get",
        loadtest: {
          stages: [
            {duration: "30s",target: 50},
            {duration: "2m",target: 50},
            {duration: "30s",target: 0}
          ],
          thresholds: {
            http_req_duration: ["p(95) < 2000"], // Tiempo total de una solicitud HTTP hasta recibir completamente la respuesta.
            http_req_failed: ["rate < 0.01"], // Proporción de solicitudes HTTP fallidas respecto al total.
            waiting_time: ["avg < 1500"], // Tiempo promedio de espera del cliente por la respuesta del servidor.
            Errors: ["count < 5"], // Número total de errores ocurridos durante la prueba.
            iterations: ["count > 3000"], // Total de iteraciones completas ejecutadas en la prueba.
            http_req_waiting: ["p(95) < 500"], // Tiempo entre el envío de la solicitud y recepción del primer byte (TTFB).
            http_req_blocked: ["p(95) < 300"], // Tiempo en procesar la solicitud debido a DNS lookup o bloqueos.
            active_vus: ["avg > 20"], // Promedio de usuarios virtuales activos durante la prueba.
            http_req_connecting: ["p(95) < 200"], // Tiempo para establecer la conexión TCP con el servidor.
            transactions_per_second: ["avg >= 50"] // Promedio de transacciones procesadas por segundo.
          }
        },
        stresstest: {
          stages: [
            {duration: "30s", target: 200},
            {duration: "1m", target: 50},
            {duration: "1m", target: 75},
            {duration: "30s", target: 0}
          ],
          thresholds: {
            http_req_duration: ["p(95) < 5000"],
            waiting_time: ["avg < 3500"],
            Errors: ["count < 20"],
            http_req_failed: ["rate < 0.01"],
            http_req_connecting: ["p(95) < 1000"],
            http_req_blocked: ["p(95) < 500"],
            iterations: ["count > 10000"],
            vus_max: ["value >= 0"],
            http_req_waiting: ["p(99) < 10000"],
            retries: ["count < 10"]
          }
        },
        peaktest: {
          stages: [
            {duration: "1m", target: 5},
            {duration: "30s", target: 300},
            {duration: "1m", target: 10},
            {duration: "30s", target: 0}
          ],
          thresholds: {
            http_req_duration: ["p(95) < 3000"],
            waiting_time: ["avg < 2000"],
            Errors: ["count < 10"],
            http_req_failed: ["rate < 0.01"],
            http_req_receiving: ["p(95) < 1000"],
            http_req_tls_handshaking: ["p(95) < 500"],
            http_req_blocked: ["p(95) < 300"],
            vus_max: ["value >= 0"],
            iterations: ["count > 3000"],
            checks: ["rate > 0.95"],
            http_req_waiting: ["p(95) < 3000"]
          }
        },
        soakingtest: {
          stages: [
            {duration: "30s", target: 200},
            {duration: "2m", target: 200},//3h
            {duration: "30s", target: 0},
          ],
          thresholds: {
            http_req_duration: ["p(95) < 2000"],
            waiting_time: ["avg < 1200"],
            Errors: ["count <= 2"],
            http_req_failed: ["rate < 0.01"],
            http_req_sending: ["p(95) < 500"],
            transaction_duration: ['avg<2'],           // Duración media de transacciones
            transactions_per_second: ['avg>=20'],      // Transacciones por segundo
            iterations: ["count > 10000"],
            checks: ["rate > 0.99"]
          }
        },
        capacitytest: {
          stages: [
            { duration: '30s', target: 10 }, // Inicio gradual con 50 usuarios
            { duration: '1m', target: 40 }, // Incremento a 200 usuarios
            { duration: '1m', target: 30 }, // Incremento máximo a 400 usuarios
            { duration: '30s', target: 0 },  // Reducción a 0 usuarios
          ],
          thresholds: {
            http_req_duration: ['p(95)<3000'], // Respuesta aceptable en 95% de las solicitudes
            waiting_time: ["avg < 1200"],
            Errors: ["count < 2"],
            http_req_failed: ['rate<0.05'],    // Máximo 5% de fallos permitidos
          },
        },
        stabilitytest: {
          stages: [
           { duration: '30s', target: 25 }, // Ramp-up inicial
           { duration: '2m', target: 50 }, // Mantén 100 usuarios durante 1 hora
           { duration: '30s', target: 0 },   // Ramp-down al final
          ],
          thresholds: {
            http_req_duration: ['avg<2000', 'p(95)<2500'],
            waiting_time: ["avg < 1200"],
            Errors: ["count < 2"],
            http_req_failed: ['rate<0.01'],
          },
        }

    }
  ]
}