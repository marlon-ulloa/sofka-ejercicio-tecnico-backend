/* ===========================================
   TABLA: personas
   Descripción: Almacena la información de las personas.
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
