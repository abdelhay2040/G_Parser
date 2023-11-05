package com.gerimedica.parser.repository;

import com.gerimedica.parser.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {

}
