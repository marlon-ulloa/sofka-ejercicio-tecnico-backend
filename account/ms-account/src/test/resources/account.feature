Feature: Test of Accounts
  Background:
    * url 'http://localhost:8081/api'

  Scenario: Create Account
    Given path 'cuentas'
    And request { "tipoCuenta": "Ahorros", "saldoInicial": 2000, "estado": true, "identification": "0105131221" }
    When method post
    Then status 201



  Scenario: Get All Accounts
    Given path 'cuentas'
    When method get
    Then status 200

  Scenario: Get Account Status Report by Client and Date Range
    Given path 'reportes'
    And param clienteId = '0105131221'
    And param fechaInicio = '2025-03-08T00:00:00'
    And param fechaFin = '2025-03-16T23:59:59'
    When method get
    Then status 200
    And print response
