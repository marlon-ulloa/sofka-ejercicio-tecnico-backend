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
