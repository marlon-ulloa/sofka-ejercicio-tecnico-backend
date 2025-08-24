package com.sofka.msclient.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Clase DTO para manejo de informacion de cliente
 */
public class ClientDTO {

    private int id;
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
    @NotBlank(message = "El genero no puede estar vacío")
    private String genero;
    @NotNull(message = "La edad no puede estar vacía")
    private int edad;
    @NotBlank(message = "La identificación no puede estar vacía")
    private String identificacion;
    @NotBlank(message = "La dirección no puede estar vacía")
    private String direccion;
    @NotBlank(message = "El telefono no puede estar vacío")
    private String telefono;
    @NotBlank(message = "La contrasena no puede estar vacía")
    private String contrasena;
    @NotNull(message = "El estado no puede estar vacío")
    private Boolean estado;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
