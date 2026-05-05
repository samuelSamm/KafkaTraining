package com.tecnosolution.KafkaTraining.kafka;

import com.tecnosolution.KafkaTraining.model.Orden;
import com.tecnosolution.KafkaTraining.repository.OrdenRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class InventarioConsumer {

    private final OrdenRepository ordenRepository;

    public InventarioConsumer(OrdenRepository ordenRepository) {
        this.ordenRepository = ordenRepository;
    }

    @KafkaListener(topics = "ordenes-creadas", groupId = "grupo-inventario")
    public void consumirOrden(String idOrden) {
        // 1. IDEMPOTENCIA: Verificar si ya existe en la BD
        if (ordenRepository.existsById(idOrden)) {
            System.out.println("⚠️ Mensaje duplicado detectado: " + idOrden + ". Saltando procesamiento.");
            return; // Salimos del método sin hacer nada
        }

        System.out.println("Consumer: Mensaje recibido de Kafka -> " + idOrden);

        // Simulamos el procesamiento guardando en H2
        Orden nuevaOrden = new Orden(idOrden, "PROCESADA_POR_INVENTARIO");
        ordenRepository.save(nuevaOrden);

        System.out.println("Consumer: Orden guardada exitosamente en la base de datos H2.");
    }
}