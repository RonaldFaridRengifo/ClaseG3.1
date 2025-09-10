package com.ronaldfrengifo.product_backend.controller;

import com.ronaldfrengifo.product_backend.entity.Producto;
import com.ronaldfrengifo.product_backend.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {
    
    @Autowired
    private ProductoService productoService;
    
    /**
     * Obtiene todos los productos con información adicional
     * GET /api/productos
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProductos() {
        try {
            List<Producto> productos = productoService.findAll();
            long total = productoService.count();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Productos obtenidos exitosamente");
            response.put("data", productos);
            response.put("total", total);
            response.put("count", productos.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error al obtener productos: " + e.getMessage());
            errorResponse.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Obtiene un producto por ID con información detallada
     * GET /api/productos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProductoById(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "ID de producto inválido");
                errorResponse.put("data", null);
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            Optional<Producto> producto = productoService.findById(id);
            Map<String, Object> response = new HashMap<>();
            
            if (producto.isPresent()) {
                response.put("success", true);
                response.put("message", "Producto encontrado exitosamente");
                response.put("data", producto.get());
            } else {
                response.put("success", false);
                response.put("message", "Producto no encontrado con ID: " + id);
                response.put("data", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error al buscar producto: " + e.getMessage());
            errorResponse.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Crea un nuevo producto con validaciones mejoradas
     * POST /api/productos
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createProducto(@RequestBody Producto producto) {
        try {
            // Validaciones mejoradas
            Map<String, String> validationErrors = validateProducto(producto);
            if (!validationErrors.isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Datos de producto inválidos");
                errorResponse.put("errors", validationErrors);
                errorResponse.put("data", null);
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            Producto nuevoProducto = productoService.save(producto);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Producto creado exitosamente");
            response.put("data", nuevoProducto);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error al crear producto: " + e.getMessage());
            errorResponse.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Actualiza un producto existente con validaciones mejoradas
     * PUT /api/productos/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateProducto(@PathVariable Long id, @RequestBody Producto producto) {
        try {
            // Validar ID
            if (id == null || id <= 0) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "ID de producto inválido");
                errorResponse.put("data", null);
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            // Validar datos del producto
            Map<String, String> validationErrors = validateProducto(producto);
            if (!validationErrors.isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Datos de producto inválidos");
                errorResponse.put("errors", validationErrors);
                errorResponse.put("data", null);
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            // Verificar si el producto existe
            if (!productoService.existsById(id)) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Producto no encontrado con ID: " + id);
                errorResponse.put("data", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            
            // Establecer el ID del producto y actualizar
            producto.setId(id);
            Producto productoActualizado = productoService.save(producto);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Producto actualizado exitosamente");
            response.put("data", productoActualizado);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error al actualizar producto: " + e.getMessage());
            errorResponse.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Elimina un producto por ID con confirmación
     * DELETE /api/productos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteProducto(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "ID de producto inválido");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            if (!productoService.existsById(id)) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Producto no encontrado con ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            
            // Obtener el producto antes de eliminarlo para mostrar información
            Optional<Producto> productoEliminado = productoService.findById(id);
            productoService.deleteById(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Producto eliminado exitosamente");
            response.put("data", productoEliminado.orElse(null));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error al eliminar producto: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Elimina múltiples productos por IDs
     * DELETE /api/productos/batch
     */
    @DeleteMapping("/batch")
    public ResponseEntity<Map<String, Object>> deleteProductos(@RequestBody List<Long> ids) {
        try {
            if (ids == null || ids.isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Lista de IDs no puede estar vacía");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            int eliminados = 0;
            int noEncontrados = 0;
            
            for (Long id : ids) {
                if (productoService.existsById(id)) {
                    productoService.deleteById(id);
                    eliminados++;
                } else {
                    noEncontrados++;
                }
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Eliminación masiva completada");
            response.put("eliminados", eliminados);
            response.put("noEncontrados", noEncontrados);
            response.put("totalProcesados", ids.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error en eliminación masiva: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Obtiene estadísticas de productos
     * GET /api/productos/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getProductoStats() {
        try {
            long total = productoService.count();
            List<Producto> productos = productoService.findAll();
            
            double precioPromedio = productos.stream()
                .mapToDouble(Producto::getPrecio)
                .average()
                .orElse(0.0);
            
            double precioMinimo = productos.stream()
                .mapToDouble(Producto::getPrecio)
                .min()
                .orElse(0.0);
            
            double precioMaximo = productos.stream()
                .mapToDouble(Producto::getPrecio)
                .max()
                .orElse(0.0);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Estadísticas obtenidas exitosamente");
            response.put("data", Map.of(
                "totalProductos", total,
                "precioPromedio", Math.round(precioPromedio * 100.0) / 100.0,
                "precioMinimo", precioMinimo,
                "precioMaximo", precioMaximo
            ));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error al obtener estadísticas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Busca productos por nombre (búsqueda parcial)
     * GET /api/productos/search?nombre={nombre}
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchProductos(@RequestParam String nombre) {
        try {
            if (nombre == null || nombre.trim().isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Término de búsqueda no puede estar vacío");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            List<Producto> productos = productoService.findAll().stream()
                .filter(p -> p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .toList();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Búsqueda completada");
            response.put("data", productos);
            response.put("total", productos.size());
            response.put("termino", nombre);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error en búsqueda: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Método privado para validar datos del producto
     */
    private Map<String, String> validateProducto(Producto producto) {
        Map<String, String> errors = new HashMap<>();
        
        if (producto == null) {
            errors.put("producto", "El producto no puede ser nulo");
            return errors;
        }
        
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            errors.put("nombre", "El nombre del producto es requerido");
        } else if (producto.getNombre().length() < 2) {
            errors.put("nombre", "El nombre debe tener al menos 2 caracteres");
        } else if (producto.getNombre().length() > 100) {
            errors.put("nombre", "El nombre no puede exceder 100 caracteres");
        }
        
        if (producto.getPrecio() == null) {
            errors.put("precio", "El precio del producto es requerido");
        } else if (producto.getPrecio() <= 0) {
            errors.put("precio", "El precio debe ser mayor a 0");
        } else if (producto.getPrecio() > 999999.99) {
            errors.put("precio", "El precio no puede exceder 999,999.99");
        }
        
        return errors;
    }
}