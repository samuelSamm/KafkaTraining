package com.tecnosolution.KafkaTraining.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class VentaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public VentaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void enviarOrden(String idOrden) {
        kafkaTemplate.send("ordenes-creadas", idOrden);
        System.out.println("Producer: Orden enviada a Kafka -> " + idOrden);
    }
}
