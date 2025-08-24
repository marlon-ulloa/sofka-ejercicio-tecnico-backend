package com.sofka.msclient.entity;

import jakarta.persistence.*;

/**
 * Clase entity para el manejo de la persistencia de la tabla clientes
 */
@Entity
@Table(name = "clientes")
@PrimaryKeyJoinColumn(name = "personaid", referencedColumnName = "id")
public class Client extends Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // clave primaria

    private String contrasena;
    private Boolean estado;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
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
