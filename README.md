# Prueba Técnica

## Instrucciones para desplegar el proyecto completo

1. Dentro del directorio del proyecto, abrir una terminal y ejecutar el siguietne comando:
   ```
   docker compose up --build
   ```
2. Una vez ejecutado el comando, se iniciará la descarga de los contenedores y posterior el levantamiento de todos los aplicativos necesarios.

## Instrucciones para ejecutar las pruebas unitarias y de integración
Las pruebas unitarias se realizaron con la librería de Junit 5, y las pruebas de integhración con Mvc de Spring y Karate DSL.
Todas las pruebas se ejecutan a través del comando `mvn test` en ambos microservicios. Tomar en cuenta que debe ser la última version de maven 3.9.9 y la versión 21 de jdk de JAVA.\
**Importante: Para la ejecucion de las pruebas es necesario primero levantar todos los contenedores, posterior a ello, en los archivos `application.properties` (ruta: nombremicroservicio/src/main/resources/) de ambos microservicios se debe cambiar en ambos microservicios la línea: `spring.kafka.bootstrap-servers=kafka:9092` por `spring.kafka.bootstrap-servers=localhost:29092`. Adicional, en el mismo archivo, en el microservicio msclient cambiar la línea `spring.datasource.url=jdbc:postgresql://db_cliente:5432/clientedb` por `spring.datasource.url=jdbc:postgresql://localhost:5433/clientedb` y en el microservicio msaccount cambiar la línea `spring.datasource.url=jdbc:postgresql://db_cuenta:5432/cuentadb` por `spring.datasource.url=jdbc:postgresql://localhost:5434/cuentadb`. COn esos cambios se puede conectar a los contenedores el momento de realizar las pruebas**
Para ejecutar las pruebas de forma individual, es decir, primero las pruebas unitarias y luego las pruebas de integración se deben seguir los siguientes pasos:
### Microservicio msclient
1. Ejecución de pruebas unitarias:
   ```
   mvn -Dtest=ClientServiceTest test
   ```
<img width="725" height="696" alt="cliente unittest" src="https://github.com/user-attachments/assets/e7f4e636-1290-47a5-8e9d-8d389e4f3fce" />
2. Ejecución de pruebas de integración con Mvc de Spring:
  ```
   mvn -Dtest=ClientControllerIntegrationTest test
   ```

3. Ejecución de pruebas de integración con Karate:
  ```
   mvn -Dtest=ClientTest test
   ```
<img width="1024" height="521" alt="clientkarate" src="https://github.com/user-attachments/assets/0c1ee57a-b3a8-475d-acab-daf482f802f7" />

### Microservicio msaccount
1. Ejecución de pruebas unitarias:
   ```
   mvn -Dtest=AccountServiceTest test
   mvn -Dtest=ConversionsTest test
   ```
<img width="872" height="536" alt="accountunittest1" src="https://github.com/user-attachments/assets/d5acddd9-349c-49f5-b28f-ab320346b176" />
<img width="680" height="535" alt="accountunittest2" src="https://github.com/user-attachments/assets/f8b7ce4d-a0b9-43ed-b08a-ad52cadbf065" />

2. Ejecución de pruebas de integración con Mvc de Spring:
  ```
   mvn -Dtest=AccountControllerIntegrationTest test
   ```
3. Ejecución de pruebas de integración con Karate:
  ```
   mvn -Dtest=AccountTest test
   ```
## Cobertura de Código
Se implementó añadió framework de jacoco en el pom de los dos proyectos para obtener la cobertura de código de las pruebas, para ello, una vez que se jecuten las pruebas, sea de manera general o individual, en la carpeta **target** de cada microservicio, ecistirá un directorio **jacoco-report**, dentro del cual se encuentra un archivo **index.html**, si lo abrimos veremos el porcentaje de cobertura:
<img width="1145" height="373" alt="ejemplo code coverage" src="https://github.com/user-attachments/assets/15906b5c-b0aa-4b29-806d-a99c561440d9" />

## Instrucciones para consumir los endpoints
1. Dentro de la carpeta **postmancollection** se encuentra el archivo en formato json, solo se debe importar ese archivo en la herrmaienta postman.
2. Una vez importado el archivo, tendremos algo similar a la siguiente imagen:
   <img width="319" height="329" alt="postman" src="https://github.com/user-attachments/assets/1fb6291e-9f25-4b4d-8d8e-a02baa353def" />
3. Ejecutar el Endpoint **CREATE CLIENTE**:
<img width="403" height="590" alt="crear cliente" src="https://github.com/user-attachments/assets/6c03914f-0c67-4f1b-b4a2-025307b93336" />

4. Ejecutar el Endpoint **CREATE ACCOUNT**:
<img width="418" height="620" alt="crear cuenta" src="https://github.com/user-attachments/assets/84634c0b-7753-44ac-b5bc-a982083c6515" />

5. Ejecutar el Endpoint **CREAR MOVIMIENTO**:
<img width="388" height="691" alt="Crear movimiento" src="https://github.com/user-attachments/assets/31be46f0-5a5e-441b-9bc0-f3a94d93d2a4" />

6. Ejecutar el Endpoint **REPORTE ESTADO CUENTA**:
<img width="437" height="685" alt="reporte estado cuenta" src="https://github.com/user-attachments/assets/bcb522d4-512b-47fb-99ae-93c8ccba6a69" />

7. Ejecutar el Endpoint **OBTENER TODOS LOS CLIENTES**:
<img width="431" height="713" alt="listado-clientes" src="https://github.com/user-attachments/assets/d4d0963d-5c21-42bb-9dae-37201181f31f" />
  
**Los demás endpoints que se encuentran en el postman collection se ejecutan de la misma manera que los descritos anteriormente.***

## Ejecución de Pruebas de Rendimiento
Para ejecutar las pruebas de rendimiento es neceario instalar Grafana K6.
Ingresar en el directorio performancetest y ejecutar el siguiente comando:
    ```
       k6 run --env TEST_TYPE=loadtest performancetest.js
    ```
Para la variable de entorno TEST_TYPE podemos colocar cualquiera de las siguientes dependiendo del tipo de prueba que deseemos hacer:\
    ```
       loadtest 
       stresstest
       peaktest
       soakingtest
       capacitytest
       stabilitytest
    ```
<img width="1734" height="1238" alt="pruebas de rendimeinto_v1" src="https://github.com/user-attachments/assets/b305f32e-2c68-40bd-881c-4eb090f6d6be" />



