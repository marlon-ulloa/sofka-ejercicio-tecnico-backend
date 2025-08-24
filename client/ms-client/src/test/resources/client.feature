Feature: Test Of Clients

  Background:
    * url 'http://localhost:8080'

  Scenario: Create a client
    Given path 'clientes'
    And request {"nombre": "Jose Lema", "genero": "Masculino", "edad": 60, "identificacion": "123456745", "direccion": "Otavalo sn y principal", "telefono": "098254786", "contrasena": "1234", "estado": true}
    When method post
    Then status 201


  Scenario: Obtener todos los clientes
    Given path 'clientes'
    When method get
    Then status 200