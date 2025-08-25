import http from 'k6/http';
import { check, sleep } from 'k6';
import { Trend, Counter } from 'k6/metrics';
import config from './config.js';


const myTrend = new Trend('waiting_time');
const CounterErrors = new Counter('Errors');
const totalReqs = new Counter('Total_Reqs');
const totalRetries = new Counter('retries');//retries
const transactionDuration = new Trend('transaction_duration');
const transactionsPerSecond = new Trend('transactions_per_second');
const activeVUs = new Trend('active_vus');

// Obtener el escenario especificado (tipo de prueba) a través de la variable de entorno
const scenarioName = __ENV.TEST_TYPE;

if (!scenarioName) {
    throw new Error('Debes especificar el tipo de prueba con --env TEST_TYPE=<nombre_del_tipo_de_prueba>');
}

// Filtrar los endpoints que tienen el escenario especificado
const endpoints = config.endpoints.filter((endpoint) => endpoint[scenarioName]);

if (endpoints.length === 0) {
    throw new Error(`No se encontró el tipo de prueba '${scenarioName}' en ningún endpoint del archivo`);
}

const scenarios = {}; // Almacena todos los escenarios para options.scenarios
const thresholds = {}; // Almacena todos los thresholds para options.thresholds

// Recorre cada uno de los endpoints delarchivo de configuración
endpoints.forEach((endpoint, index) => {
    const scenarioKey = scenarioName + "_endpoint_" + index; // Arma la llave para identificar entre endpoints
    const scenarioConfig = endpoint[scenarioName];

    scenarios[scenarioKey] = {
        executor: 'ramping-vus', // Tipo de executor, puede cambiarse según necesidad
        stages: scenarioConfig.stages,
        exec: "genericScenario", // Ejecuta la funcion generica para todos los escenarios y endpoints
        tags: { endpoint: scenarioKey }, // Agrega un tag unico por cada escenario y endpoint, con ello se puede ejecutar de manera dinamica n tipos de prueba para n endpoints
        env: { // Almacena en variables de entorno para cada escenario y endpoint el url y la llave unica
            TEST_URL: endpoint.url,
            PAYLOAD: endpoint.payload,
            METHOD_TYPE: endpoint.method,
            SCENARIO_TAG: scenarioKey,
        },
    };

    // Agregar thresholds específicos de este escenario
    // Como los thresholds pueden variar entre tipos de prueba y escenarios entonces solo se dbeen ejecutar los thresholds que corresponden al tipo de prueba de cada endpoint
    if (scenarioConfig.thresholds) {
        Object.entries(scenarioConfig.thresholds).forEach(([metric, rules]) => {
            const uniqueMetricName = metric + "{endpoint:" + scenarioKey + "}"; // Para ejecutar los thresholds popr cada llave, se debe hacer referencia al tag cada metrica
            thresholds[uniqueMetricName] = rules;
        });
    }
});
// Exporta las opciones a considerarse en el momento de la ejecucion
export const options = {
    insecureSkipTLSVerify: true, // Esquivar la conexion tls
    scenarios: scenarios,
    thresholds: thresholds
};
export function genericScenario(){
    const start = Date.now();
    activeVUs.add(__VU);
    let maxRetries = 3; // Número máximo de reintentos permitidos por solicitud
    let retries = 0;
    let response;

    // Intentar enviar la solicitud con reintentos
    if(__ENV.TEST_TYPE === 'estres'){
        do{
            if(__ENV.METHOD_TYPE === 'post'){
                response = post(__ENV.TEST_URL, __ENV.PAYLOAD, __ENV.SCENARIO_TAG);
            }
            ///cuando se requiere usar otros metodos
            else if(__ENV.METHOD_TYPE === 'get'){
		response = get(__ENV.TEST_URL, __ENV.PAYLOAD, __ENV.SCENARIO_TAG)
            }

            retries ++;
            let status = response.status === 200;
            CounterErrors.add(!status);
            totalReqs.add(1);
        }while(response.status !== 200 && retries <= maxRetries);
        if(retries > 1){
            totalRetries.add(retries - 1);
        }
    }else{//cuando no se necesite la variable de retries a ser evaluada como metrica
        if(__ENV.METHOD_TYPE === 'post'){
                response = post(__ENV.TEST_URL, __ENV.PAYLOAD, __ENV.SCENARIO_TAG);
            }
            ///cuando se requiere usar otros metodos
        else if(__ENV.METHOD_TYPE === 'get'){
		response = get(__ENV.TEST_URL, __ENV.PAYLOAD, __ENV.SCENARIO_TAG)
        }

        totalReqs.add(1);
    }
    // Calcula la duración en segundos
    const durationInSeconds = (Date.now() - start) / 1000;
    //Añade a la metrica la duración en segundos
    transactionDuration.add(durationInSeconds);
    // Calcula las transacciones por segundo y añade a la métrica
    if(durationInSeconds > 0) {
        transactionsPerSecond.add(1 / durationInSeconds);
    }
    // Validar el estado de la respuesta
    check(response, {
        'is status 200': (r) => r.status == 200,
    });

    let status = response.status === 200;
    myTrend.add(response.timings.waiting);
    CounterErrors.add(!status);

    sleep(1);// Simulación de pausa entre transacciones
}

function post(url, payload, scenarioTag){
    return http.post(
                   url, // Se obtiene la URL almacenada en la definicion del escenario anteriormente
                   payload,
                       {
                           headers: { 'Content-Type': 'application/json' },
                           tags: {endpoint: scenarioTag}, // Se ejecuta unicamente el tag correspondiente
                       }
                    );
}
function get(url, params, scenarioTag){
    if(params){
        // Convertir los parámetros a una cadena de consulta
        const queryString = new URLSearchParams(params).toString();

        // Agregar los parámetros a la URL
        const url = `${url}?${queryString}`;
    }
    return http.get(
               url,
               {
                   tags: { endpoint: scenarioTag }, // Tag para métricas
               }
           );
}
function put(url, payload, scenarioTag){
    return http.put(
               url, // URL del recurso a actualizar
               payload, // El payload en formato JSON
               {
                   headers: { 'Content-Type': 'application/json' }, // Headers para indicar JSON
                   tags: { endpoint: scenarioTag }, // Tag para métricas
               }
           );
}
function deleteRequest(url, payload, scenarioTag){
    return http.del(
        url, // URL del recurso a eliminar
        payload & null, // Payload opcional (en este caso, null)
        {
            headers: { 'Content-Type': 'application/json' }, // Headers para indicar JSON
            tags: { endpoint: scenarioTag }, // Tag para métricas
        }
    );
}
function patch(url, payload, scenarioTag){
    return http.patch(
               url, // URL del recurso
               payload, // El payload parcial en formato JSON
               {
                   headers: { 'Content-Type': 'application/json' }, // Headers
                   tags: { endpoint: scenarioTag }, // Tag para métricas
               }
           );
}