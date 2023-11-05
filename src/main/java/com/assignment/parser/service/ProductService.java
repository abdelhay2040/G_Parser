package com.assignment.parser.service;

import com.assignment.parser.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    void uploadCsvFile(MultipartFile file);
    List<Product> getAllProducts();
    Product getProductByCode(String code);
    void deleteAllProducts();
}