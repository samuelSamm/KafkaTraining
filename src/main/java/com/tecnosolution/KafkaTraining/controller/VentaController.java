package com.tecnosolution.KafkaTraining.controller;

import com.tecnosolution.KafkaTraining.kafka.VentaProducer;
import com.tecnosolution.KafkaTraining.model.Orden;
import com.tecnosolution.KafkaTraining.service.OrdenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaProducer ventaProducer;
    private final OrdenService ordenService;

    public VentaController(VentaProducer ventaProducer, OrdenService ordenService) {
        this.ventaProducer = ventaProducer;
        this.ordenService = ordenService;
    }

    @PostMapping("/crear/{idOrden}")
    public ResponseEntity<String> crearOrden(@PathVariable String idOrden, Principal principal) {
        System.out.println("En el controller de Venta, podemos obtener el nombre del usuario para log de auditoria.");
        System.out.println("Usuario: "+principal.getName());
        // Llamamos al producer para iniciar el flujo asíncrono
        ventaProducer.enviarOrden(idOrden);
        return ResponseEntity.ok("Orden " + idOrden + " recibida y enviada a la cola de procesamiento. Por el usuario: "+principal.getName());
    }

    @GetMapping("/orden/{id}")
    public Orden consultarOrden(@PathVariable String id) {
        return ordenService.obtenerOrden(id);
    }

    @PutMapping("/actualizar")
    public Orden actualizarOrden(@RequestBody Orden orden){
        return ordenService.actualizarOrden(orden);
    }
}