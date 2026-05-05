package com.tecnosolution.KafkaTraining.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.io.Serializable;

@Entity
public class Orden implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String idOrden;
    private String estado;

    public Orden() {}

    public Orden(String idOrden, String estado) {
        this.idOrden = idOrden;
        this.estado = estado;
    }

    // Getters y Setters
    public String getIdOrden() { return idOrden; }
    public void setIdOrden(String idOrden) { this.idOrden = idOrden; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
