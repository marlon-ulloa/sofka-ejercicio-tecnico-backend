-- BaseDatos.sql
CREATE DATABASE client_db;
CREATE DATABASE account_db;

-- Client DB Schema
\c client_db;

CREATE TABLE personas (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    genero VARCHAR(20),
    edad INT NOT NULL,
    identificacion VARCHAR(20) UNIQUE NOT NULL,
    direccion VARCHAR(200),
    telefono VARCHAR(20)
);

CREATE TABLE clientes (
    id SERIAL PRIMARY KEY,
    personaId INT REFERENCES personas(id),
    contrasena VARCHAR(100) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE
);

-- Account DB Schema
\c account_db;

CREATE TABLE cuentas (
    id SERIAL PRIMARY KEY,
    numero_cuenta VARCHAR(20) UNIQUE NOT NULL,
    tipo_cuenta VARCHAR(50) NOT NULL,
    saldo_inicial DECIMAL(15, 2) NOT NULL,
    saldo_actual DECIMAL(15, 2) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    cliente_id BIGINT NOT NULL
);

CREATE TABLE movimientos (
    id SERIAL PRIMARY KEY,
    cuenta_id INT REFERENCES cuentas(id),
    numero_cuenta VARCHAR(20) REFERENCES cuentas(numero_cuenta),
    fecha TIMESTAMP NOT NULL,
    tipo_movimiento VARCHAR(50) NOT NULL,
    valor DECIMAL(15, 2) NOT NULL,
    saldo DECIMAL(15, 2) NOT NULL
);

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

CREATE SEQUENCE ahorros_seq START 1;
CREATE SEQUENCE corriente_seq START 1;