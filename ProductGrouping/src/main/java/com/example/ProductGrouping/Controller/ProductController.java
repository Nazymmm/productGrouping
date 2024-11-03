package com.example.ProductGrouping.Controller;

import com.example.ProductGrouping.Model.Product;
import com.example.ProductGrouping.Model.ProductGroup;
import com.example.ProductGrouping.Repository.ProductGroupRepository;
import com.example.ProductGrouping.Repository.ProductRepository;
import com.example.ProductGrouping.Service.ProductGroupingService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductGroupRepository productGroupRepository;

    @Autowired
    private ProductGroupingService productGroupingService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadProducts(@RequestParam("file") MultipartFile file) {
        try {
            List<Product> products = new ArrayList<>();
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Пропускаем заголовок

                Product product = new Product();
                product.setName(row.getCell(0).getStringCellValue());
                product.setUnit(row.getCell(1).getStringCellValue());
                product.setPricePerUnit(row.getCell(2).getNumericCellValue());
                product.setQuantity((int) row.getCell(3).getNumericCellValue());

                // Проверка на null и правильность значений
                if (product.getName() == null || product.getUnit() == null ||
                    product.getPricePerUnit() <= 0 || product.getQuantity() <= 0) {
                    return ResponseEntity.badRequest().body("Некорректные данные в файле.");
                }

                products.add(product);
            }
            workbook.close();

            productRepository.saveAll(products);
            return ResponseEntity.ok("Успешно загружено " + products.size() + " товаров.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Не удалось загрузить товары: " + e.getMessage());
        }
    }


    @GetMapping("/groups")
    public List<ProductGroup> getProductGroups() {
        List<ProductGroup> groups = productGroupRepository.findAll();
        System.out.println("Fetched groups: " + groups.size());
        return groups;
    }

    @GetMapping("/groups/{id}")
    public ResponseEntity<ProductGroup> getGroupById(@PathVariable Long id) {
        return productGroupRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }



}
