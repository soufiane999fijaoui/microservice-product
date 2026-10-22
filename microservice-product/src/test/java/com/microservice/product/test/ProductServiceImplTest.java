package com.microservice.product.test;

import java.util.List;

import com.microservice.product.config.AppConfig;
import com.microservice.product.entity.Product;
import com.microservice.product.exception.ResourceNotFoundException;
import com.microservice.product.service.ProductService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
@TestPropertySource(properties = {
        "jdbc.driver=org.h2.Driver",
        "jdbc.url=jdbc:h2:mem:product_db;DB_CLOSE_DELAY=-1",
        "jdbc.username=sa",
        "jdbc.password=",
        "hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "hibernate.show_sql=false",
        "hibernate.hbm2ddl.auto=create-drop"
})
@Transactional
class ProductServiceImplTest {

    @Autowired
    private ProductService productService;

    @Test
    void addProductPersistsEntity() {
        productService.addProduct(new Product("Tablette 11 pouces", 599.00));

        assertEquals(1L, productService.count());
        assertEquals(1, productService.getAllProducts().size());
    }

    @Test
    void updateProductChangesValues() {
        Product product = new Product("Imprimante", 199.00);
        productService.addProduct(product);

        product.setPrice(149.00);
        productService.updateProduct(product);

        assertEquals(149.00, productService.getProduct(product.getId()).getPrice(), 0.001);
    }

    @Test
    void deleteProductRemovesEntity() {
        Product product = new Product("Disque dur externe", 89.00);
        productService.addProduct(product);

        productService.deleteProduct(product.getId());

        assertThrows(ResourceNotFoundException.class, () -> productService.getProduct(product.getId()));
    }

    @Test
    void searchProductsFiltersByName() {
        productService.addProduct(new Product("Casque Bluetooth", 129.00));
        productService.addProduct(new Product("Enceinte portable", 79.00));

        List<Product> results = productService.searchProducts("casque");

        assertEquals(1, results.size());
        assertEquals("Casque Bluetooth", results.get(0).getName());
    }

    @Test
    void negativePriceIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> productService.addProduct(new Product("Produit invalide", -10.00)));
    }
}
