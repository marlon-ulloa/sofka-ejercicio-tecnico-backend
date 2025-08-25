-- BaseDatos.sql
-- Crear las bases de datos
CREATE DATABASE clientdb;
CREATE DATABASE accountdb;

-- Conectar a la base de datos client_db
\c clientdb;

/* ===========================================
   TABLA: cuentas
   Descripción: Almacena la información de las cuentas bancarias de los clientes.
=========================================== */
CREATE TABLE personas (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    genero VARCHAR(20),
    edad INT NOT NULL,
    identificacion VARCHAR(20) UNIQUE NOT NULL,
    direccion VARCHAR(200),
    telefono VARCHAR(20)
);

/* ===========================================
   TABLA: clientes
   Descripción: Almacena la información de los clientes.
=========================================== */
CREATE TABLE clientes (
    id SERIAL PRIMARY KEY,
    personaId INT REFERENCES personas(id),
    contrasena VARCHAR(100) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE
);

-- Conectar a la base de datos account_db
\c accountdb;

/* ===========================================
   TABLA: cuentas
   Descripción: Almacena la información de las cuentas bancarias de los clientes.
=========================================== */
CREATE TABLE cuentas (
    id SERIAL PRIMARY KEY,
    numero_cuenta VARCHAR(20) UNIQUE NOT NULL,
    tipo_cuenta VARCHAR(50) NOT NULL,
    saldo_inicial DECIMAL(15, 2) NOT NULL,
    saldo_actual DECIMAL(15, 2) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    cliente_id BIGINT NOT NULL
);
-- Índice adicional para mejorar consultas por cliente
CREATE INDEX idx_cuentas_cliente_id ON cuentas(cliente_id);

/* ===========================================
   TABLA: movimientos
   Descripción: Registra los movimientos realizados en una cuenta bancaria.
=========================================== */
CREATE TABLE movimientos (
    id SERIAL PRIMARY KEY,
    cuenta_id INT REFERENCES cuentas(id),
    numero_cuenta VARCHAR(20) REFERENCES cuentas(numero_cuenta),
    fecha TIMESTAMP NOT NULL,
    tipo_movimiento VARCHAR(50) NOT NULL,
    valor DECIMAL(15, 2) NOT NULL,
    saldo DECIMAL(15, 2) NOT NULL
);
-- Índices para mejorar rendimiento en consultas frecuentes
CREATE INDEX idx_movimientos_cuenta_id ON movimientos(cuenta_id);
CREATE INDEX idx_movimientos_numero_cuenta ON movimientos(numero_cuenta);
CREATE INDEX idx_movimientos_fecha ON movimientos(fecha);
CREATE INDEX idx_movimientos_cuenta_fecha ON movimientos(cuenta_id, fecha);

/* ===========================================
   TABLA: transacciones
   Descripción: Almacena transacciones realizadas en una cuenta (detalle más granular).
=========================================== */
CREATE TABLE transacciones (
    id SERIAL PRIMARY KEY,
    cuenta_id INT NOT NULL,
    tipo_movimiento VARCHAR(50) NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    saldo_anterior DECIMAL(10,2) NOT NULL,
    saldo_nuevo DECIMAL(10,2) NOT NULL,
    fecha TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (cuenta_id) REFERENCES cuentas(id)
);
-- Índices para consultas por cuenta y fechas
CREATE INDEX idx_transacciones_cuenta_id ON transacciones(cuenta_id);
CREATE INDEX idx_transacciones_fecha ON transacciones(fecha);
CREATE INDEX idx_transacciones_cuenta_fecha ON transacciones(cuenta_id, fecha);

/* ===========================================
   SECUENCIAS
=========================================== */
CREATE SEQUENCE ahorros_seq START 1;
CREATE SEQUENCE corriente_seq START 1;