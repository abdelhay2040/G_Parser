package com.assignment.parser.controller;

import com.assignment.parser.model.Product;
import com.assignment.parser.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/upload-csv")
    public ResponseEntity<?> uploadCsvFile(@RequestParam("file") MultipartFile file) {
        productService.uploadCsvFile(file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getProductByCode(@PathVariable String code) {
        Product product = productService.getProductByCode(code);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    @DeleteMapping({"/", ""})
    public ResponseEntity<?> deleteAllProducts() {
        productService.deleteAllProducts();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
