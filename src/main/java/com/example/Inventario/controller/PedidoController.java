package com.example.Inventario.controller;

import com.example.Inventario.model.Pedido;
import com.example.Inventario.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody Pedido pedido) {

        try {

            Pedido nuevoPedido = pedidoService.crearPedido(pedido);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(nuevoPedido);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{id}/confirmar")
public ResponseEntity<?> confirmarPedido(@PathVariable Long id) {

    try {

        Pedido pedido = pedidoService.confirmarPedido(id);

        return ResponseEntity.ok(pedido);

    } catch (IllegalArgumentException e) {

        return ResponseEntity
                .badRequest()
                .body(e.getMessage());

    } catch (RuntimeException e) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}

    @PutMapping("/{id}/cancelar")
public ResponseEntity<?> cancelarPedido(@PathVariable Long id) {

    try {

        Pedido pedido = pedidoService.cancelarPedido(id);

        return ResponseEntity.ok(pedido);

    } catch (IllegalArgumentException e) {

        return ResponseEntity
                .badRequest()
                .body(e.getMessage());

    } catch (RuntimeException e) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}

    @PutMapping("/{id}/despachar")
public ResponseEntity<?> despacharPedido(@PathVariable Long id) {

    try {

        Pedido pedido = pedidoService.despacharPedido(id);

        return ResponseEntity.ok(pedido);

    } catch (IllegalArgumentException e) {

        return ResponseEntity
                .badRequest()
                .body(e.getMessage());

    } catch (RuntimeException e) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}
    @GetMapping("/pendientes")
public ResponseEntity<?> obtenerPendientes() {

    return ResponseEntity.ok(
            pedidoService.obtenerPendientes()
    );
}
    @GetMapping("/urgentes")
public ResponseEntity<?> obtenerUrgentes() {

    return ResponseEntity.ok(
            pedidoService.obtenerUrgentes()
    );
}
    @GetMapping("/estado")
public ResponseEntity<?> obtenerPorEstado(
        @RequestParam String estado) {

    return ResponseEntity.ok(
            pedidoService.obtenerPorEstado(estado)
    );
}
    @GetMapping("/resumen")
public ResponseEntity<?> obtenerResumen() {

    return ResponseEntity.ok(
            pedidoService.obtenerResumen()
    );
}
}