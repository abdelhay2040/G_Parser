package com.assignment.parser.service;

import com.assignment.parser.model.Product;
import com.assignment.parser.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks

    private ProductServiceImpl productService;
    @MockBean
    private ProductRepository mockProductRepository;
    private MockMultipartFile file;

    @BeforeEach
    public void setup() {
        // Example CSV content
        String csvContent = "source,codeListCode,code,displayValue,longDescription,fromDate,toDate,sortingPriority\n" +
                "ZIB,ZIB001,271636001,Polsslag regelmatig,The long description is necessary,01-01-2019,,1";
        file = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                csvContent.getBytes()
        );
    }

    @Test
    public void whenUploadCsvFile_thenSaveProducts() throws Exception {
        productService.uploadCsvFile(file);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void whenGetAllProducts_thenReturnProductList() {
        when(productRepository.findAll()).thenReturn(Collections.singletonList(new Product()));
        List<Product> products = productService.getAllProducts();
        assertFalse(products.isEmpty());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void whenGetProductByCode_thenReturnProduct() {
        String code = "271636001";
        Product mockProduct = new Product();
        when(productRepository.findById(code)).thenReturn(java.util.Optional.of(mockProduct));
        Product product = productService.getProductByCode(code);
        assertEquals(mockProduct, product);
        verify(productRepository, times(1)).findById(code);
    }

    @Test
    public void whenDeleteAllProducts_thenInvokeRepositoryDelete() {
        productService.deleteAllProducts();
        verify(productRepository, times(1)).deleteAll();
    }

    // Test parsing valid date string
    @Test
    void parseValidDate() {
        String dateStr = "01-01-2019";
        LocalDate expectedDate = LocalDate.of(2019, 1, 1);
        LocalDate actualDate = productService.parseDate(dateStr);
        assertEquals(expectedDate, actualDate);
    }

    // Test parsing invalid date string
    @Test
    void parseInvalidDate() {
        String dateStr = "invalid-date";
        assertNull(productService.parseDate(dateStr));
    }

    // Test parsing empty date string
    @Test
    void parseEmptyDate() {
        String dateStr = "";
        assertNull(productService.parseDate(dateStr));
    }

    // Test parsing null date string
    @Test
    void parseNullDate() {
        assertNull(productService.parseDate(null));
    }

    // Test parsing valid sorting priority
    @Test
    void parseValidSortingPriority() {
        String sortingStr = "10";
        Integer expectedPriority = 10;
        Integer actualPriority = productService.parseSortingPriority(sortingStr);
        assertEquals(expectedPriority, actualPriority);
    }

    // Test parsing invalid sorting priority
    @Test
    void parseInvalidSortingPriority() {
        String sortingStr = "invalid";
        assertThrows(NumberFormatException.class, () -> {
            productService.parseSortingPriority(sortingStr);
        });
    }

    // Test parsing empty sorting priority
    @Test
    void parseEmptySortingPriority() {
        String sortingStr = "";
        assertNull(productService.parseSortingPriority(sortingStr));
    }

    // Test parsing null sorting priority
    @Test
    void parseNullSortingPriority() {
        assertNull(productService.parseSortingPriority(null));
    }


    // Test handling of empty file
    @Test
    void uploadEmptyCsvFile() {
       //@todo
    }

    // Test handling of invalid file format
    @Test
    void uploadInvalidCsvFile() {
        //@todo
    }

}
