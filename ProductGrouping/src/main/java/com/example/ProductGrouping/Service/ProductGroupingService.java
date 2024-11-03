package com.example.ProductGrouping.Service;

import com.example.ProductGrouping.Model.Product;
import com.example.ProductGrouping.Model.ProductGroup;
import com.example.ProductGrouping.Repository.ProductGroupRepository;
import com.example.ProductGrouping.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ProductGroupingService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductGroupRepository productGroupRepository;

    @Transactional
    public void groupProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductGroup> groupedProducts = yourGroupingLogic(products);

        for (ProductGroup group : groupedProducts) {
            ProductGroup savedGroup = productGroupRepository.save(group);

            for (Product product : group.getProducts()) {
                product.setGroup(savedGroup);
                productRepository.save(product);
            }
        }
    }

    @Scheduled(fixedRate = 300000) // Каждый 5 минут
    public void scheduleProductGrouping() {
        groupProducts();
    }

    private List<ProductGroup> yourGroupingLogic(List<Product> products) {
        // Сортируем продукты по цене в порядке убывания (можно изменить на возрастание, если нужно)
        products.sort(Comparator.comparingDouble(Product::getPrice).reversed());

        List<ProductGroup> productGroups = new ArrayList<>();
        ProductGroup currentGroup = new ProductGroup();
        currentGroup.setProducts(new ArrayList<>()); // Инициализация коллекции продуктов
        double currentTotalPrice = 0;

        for (Product product : products) {
            // Проверяем, если добавление текущего продукта не превысит лимит в 200 евро
            if (currentTotalPrice + product.getPrice() <= 200) {
                // Добавляем продукт в текущую группу
                currentGroup.addProduct(product);
                currentTotalPrice += product.getPrice();
            } else {
                // Если добавление текущего продукта превысит 200 евро,
                // ищем место для него в текущей группе или создаем новую группу
                boolean addedToGroup = false;

                // Проверяем, если есть возможность создать новую группу с продуктом
                if (product.getPrice() <= 200) {
                    // Сохраняем текущую группу, если она не пустая
                    if (!currentGroup.getProducts().isEmpty()) {
                        currentGroup.setTotalPrice(currentTotalPrice);
                        productGroups.add(currentGroup);
                    }

                    // Создаем новую группу и добавляем текущий продукт в нее
                    currentGroup = new ProductGroup();
                    currentGroup.setProducts(new ArrayList<>()); // Инициализация коллекции для новой группы
                    currentGroup.addProduct(product);
                    currentTotalPrice = product.getPrice(); // Обновляем текущую сумму
                    addedToGroup = true;
                }

                // Если не добавили в группу, пробуем сгруппировать текущие товары с учетом лимита
                if (!addedToGroup) {
                    // Здесь вы можете добавить логику для нахождения наилучшей комбинации
                    // товаров для текущей группы, например, используя динамическое
                    // программирование или жадный алгоритм
                }
            }
        }

        // Добавляем последнюю группу, если она не пустая
        if (!currentGroup.getProducts().isEmpty()) {
            currentGroup.setTotalPrice(currentTotalPrice);
            productGroups.add(currentGroup);
        }

        return productGroups;
    }
}
