package com.example.Inventario.service;

import com.example.Inventario.model.Pedido;
import com.example.Inventario.model.Producto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    private final List<Producto> productos = new ArrayList<>();
    private final List<Pedido> pedidos = new ArrayList<>();

    private Long siguienteId = 1L;

    public PedidoService() {

        productos.add(new Producto(1L, "Arroz", 20));
        productos.add(new Producto(2L, "Leche", 15));
        productos.add(new Producto(3L, "Cafe", 10));
        productos.add(new Producto(4L, "Azucar", 25));
        productos.add(new Producto(5L, "Aceite", 12));
    }

    public Pedido crearPedido(Pedido pedido) {

        if (pedido.getCliente() == null || pedido.getCliente().trim().isEmpty()) {
            throw new IllegalArgumentException("El cliente es obligatorio");
        }

        if (pedido.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }

        if (pedido.getPrioridad() == null ||
                !esPrioridadValida(pedido.getPrioridad())) {
            throw new IllegalArgumentException("La prioridad no es valida");
        }

        Producto producto = buscarProducto(pedido.getProductoId());

        if (producto == null) {
            throw new RuntimeException("Producto no encontrado");
        }

        pedido.setId(siguienteId++);
        pedido.setEstado("PENDIENTE");

        pedidos.add(pedido);

        return pedido;
    }

    public Pedido confirmarPedido(Long id) {

    Pedido pedido = null;

    // Buscar el pedido
    for (Pedido p : pedidos) {
        if (p.getId().equals(id)) {
            pedido = p;
            break;
        }
    }

    // Si el pedido no existe
    if (pedido == null) {
        throw new RuntimeException("Pedido no encontrado");
    }

    // Solo se pueden confirmar pedidos pendientes
    if (!pedido.getEstado().equals("PENDIENTE")) {
        throw new IllegalArgumentException(
                "El pedido no puede ser confirmado porque su estado es "
                        + pedido.getEstado()
        );
    }

    // Buscar el producto
    Producto producto = buscarProducto(pedido.getProductoId());

    if (producto == null) {
        throw new RuntimeException("Producto no encontrado");
    }

    // Comprobar stock
    if (producto.getStock() < pedido.getCantidad()) {
        throw new IllegalArgumentException(
                "Stock insuficiente. Stock disponible: "
                        + producto.getStock()
        );
    }

    // Descontar stock
    producto.setStock(
            producto.getStock() - pedido.getCantidad()
    );

    // Cambiar estado
    pedido.setEstado("CONFIRMADO");

    return pedido;
}

    public Pedido cancelarPedido(Long id) {

    Pedido pedido = null;

    // Buscar el pedido
    for (Pedido p : pedidos) {
        if (p.getId().equals(id)) {
            pedido = p;
            break;
        }
    }

    // Si no existe
    if (pedido == null) {
        throw new RuntimeException("Pedido no encontrado");
    }

    // No se puede cancelar un pedido ya despachado
    if (pedido.getEstado().equals("DESPACHADO")) {
        throw new IllegalArgumentException(
                "No se puede cancelar un pedido despachado"
        );
    }

    // No se puede cancelar dos veces
    if (pedido.getEstado().equals("CANCELADO")) {
        throw new IllegalArgumentException(
                "El pedido ya está cancelado"
        );
    }

    // Si estaba confirmado, devolver el stock
    if (pedido.getEstado().equals("CONFIRMADO")) {

        Producto producto = buscarProducto(pedido.getProductoId());

        if (producto != null) {
            producto.setStock(
                    producto.getStock() + pedido.getCantidad()
            );
        }
    }

    // Cambiar estado
    pedido.setEstado("CANCELADO");

    return pedido;
}
    private boolean esPrioridadValida(String prioridad) {

        return prioridad.equals("BAJA") ||
               prioridad.equals("MEDIA") ||
               prioridad.equals("ALTA") ||
               prioridad.equals("URGENTE");
    }

    private Producto buscarProducto(Long productoId) {

        for (Producto producto : productos) {

            if (producto.getId().equals(productoId)) {
                return producto;
            }
        }

        return null;
    }

    public List<Producto> obtenerProductos() {
        return productos;
    }

    public List<Pedido> obtenerPedidos() {
        return pedidos;
    }

    public Pedido despacharPedido(Long id) {

    Pedido pedido = null;

    // Buscar el pedido
    for (Pedido p : pedidos) {
        if (p.getId().equals(id)) {
            pedido = p;
            break;
        }
    }

    // Si no existe
    if (pedido == null) {
        throw new RuntimeException("Pedido no encontrado");
    }

    // Solo se pueden despachar pedidos confirmados
    if (!pedido.getEstado().equals("CONFIRMADO")) {
        throw new IllegalArgumentException(
                "Solo se pueden despachar pedidos CONFIRMADOS"
        );
    }

    // Cambiar estado
    pedido.setEstado("DESPACHADO");

    return pedido;
}
public List<Pedido> obtenerPendientes() {

    List<Pedido> resultado = new ArrayList<>();

    for (Pedido pedido : pedidos) {
        if (pedido.getEstado().equals("PENDIENTE")) {
            resultado.add(pedido);
        }
    }

    return resultado;
}

public List<Pedido> obtenerUrgentes() {

    List<Pedido> resultado = new ArrayList<>();

    for (Pedido pedido : pedidos) {
        if (pedido.getPrioridad().equals("URGENTE")) {
            resultado.add(pedido);
        }
    }

    return resultado;
}

public List<Pedido> obtenerPorEstado(String estado) {

    List<Pedido> resultado = new ArrayList<>();

    for (Pedido pedido : pedidos) {
        if (pedido.getEstado().equals(estado)) {
            resultado.add(pedido);
        }
    }

    return resultado;
}

public String obtenerResumen() {

    int total = pedidos.size();
    int pendientes = 0;
    int confirmados = 0;
    int despachados = 0;
    int cancelados = 0;
    int urgentes = 0;

    for (Pedido pedido : pedidos) {

        if (pedido.getEstado().equals("PENDIENTE")) {
            pendientes++;
        }

        if (pedido.getEstado().equals("CONFIRMADO")) {
            confirmados++;
        }

        if (pedido.getEstado().equals("DESPACHADO")) {
            despachados++;
        }

        if (pedido.getEstado().equals("CANCELADO")) {
            cancelados++;
        }

        if (pedido.getPrioridad().equals("URGENTE")) {
            urgentes++;
        }
    }

    return """
            {
                "totalPedidos": %d,
                "pendientes": %d,
                "confirmados": %d,
                "despachados": %d,
                "cancelados": %d,
                "urgentes": %d
            }
            """.formatted(
            total,
            pendientes,
            confirmados,
            despachados,
            cancelados,
            urgentes
    );
}
}