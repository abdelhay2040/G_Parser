package com.assignment.parser.service;

import com.assignment.parser.model.Product;
import com.assignment.parser.repository.ProductRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void uploadCsvFile(MultipartFile file) {
        try (
                Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withIgnoreHeaderCase()
                        .withTrim())
        ) {
            for (CSVRecord record : csvParser) {
                // Here we can directly get the values by header names
                String source = record.get("source");
                String codeListCode = record.get("codeListCode");
                String code = record.get("code");


                LocalDate fromDate = parseDate(record.get("fromDate"));
                LocalDate toDate = parseDate(record.get("toDate")); // Can be empty/null

                Product product = new Product(
                        source,
                        codeListCode,
                        code,
                        record.get("displayValue"),
                        record.get("longDescription"),
                        fromDate,
                        toDate,
                        parseSortingPriority(record.get("sortingPriority")) // Utility method to parse sortingPriority
                );
                productRepository.save(product);
            }
        } catch (Exception e) {
            log.error("Failed to parse CSV file.", e);
            throw new RuntimeException("Failed to upload CSV file", e);
        }
    }

    // Utility method to parse sortingPriority as it may be null or empty.
    Integer parseSortingPriority(String sortingPriority) {
        if (sortingPriority != null && !sortingPriority.trim().isEmpty()) {
            return Integer.valueOf(sortingPriority.trim());
        }
        return null;
    }


    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductByCode(String code) {
        return productRepository.findById(code).orElse(null);
    }

    @Override
    public void deleteAllProducts() {
        productRepository.deleteAll();
    }

    LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, dateFormatter);
        } catch (DateTimeParseException e) {
            log.error("Error parsing date: " + dateStr, e);
            return null;
        }
    }
}
