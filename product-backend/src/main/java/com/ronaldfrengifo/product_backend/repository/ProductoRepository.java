package com.ronaldfrengifo.product_backend.repository;

import com.ronaldfrengifo.product_backend.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // JpaRepository ya proporciona métodos básicos como:
    // - findAll()
    // - findById(Long id)
    // - save(Producto producto)
    // - deleteById(Long id)
    // - delete(Producto producto)
    // - count()
    // - existsById(Long id)
}
