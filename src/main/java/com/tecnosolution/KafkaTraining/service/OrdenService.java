package com.tecnosolution.KafkaTraining.service;

import com.tecnosolution.KafkaTraining.model.Orden;
import com.tecnosolution.KafkaTraining.repository.OrdenRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class OrdenService {

    private final OrdenRepository ordenRepository;

    public OrdenService(OrdenRepository ordenRepository) {
        this.ordenRepository = ordenRepository;
    }

    // Al consultar, primero busca en Redis (valor "ordenes") usando el ID como llave
    @Cacheable(value = "ordenes", key = "#id")
    public Orden obtenerOrden(String id) {
        System.out.println("🚀 Buscando en Base de Datos H2 para el ID: " + id);
        return ordenRepository.findById(id).orElse(null);
    }
}