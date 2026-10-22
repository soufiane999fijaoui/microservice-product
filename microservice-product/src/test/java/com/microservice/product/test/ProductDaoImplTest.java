package com.microservice.product.test;

import java.util.ArrayList;
import java.util.List;

import com.microservice.product.config.AppConfig;
import com.microservice.product.dao.ProductDaoImpl;
import com.microservice.product.entity.Product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
class ProductDaoImplTest {

    @Autowired
    private ProductDaoImpl productDao;

    @BeforeEach
    void setUp() {
        new ArrayList<>(productDao.findAll()).forEach(productDao::delete);
        productDao.flush();
    }

    @Test
    @DisplayName("save puis findById renvoie la meme entite")
    void saveAndFindById() {
        Product product = new Product("Clavier mecanique", 89.90);

        productDao.save(product);
        productDao.flush();

        Product found = productDao.findById(product.getId());
        assertNotNull(found);
        assertEquals("Clavier mecanique", found.getName());
        assertEquals(89.90, found.getPrice(), 0.001);
    }

    @Test
    @DisplayName("findAll retourne toutes les lignes de la table")
    void findAllReturnsEveryProduct() {
        productDao.save(new Product("Ecran 27 pouces", 349.00));
        productDao.save(new Product("Souris ergonomique", 45.00));
        productDao.flush();

        List<Product> products = productDao.findAll();
        assertEquals(2, products.size());
        assertEquals(2L, productDao.count());
    }

    @Test
    @DisplayName("update modifie une entite existante")
    void updatePersistsChanges() {
        Product product = new Product("Casque audio", 199.00);
        productDao.save(product);
        productDao.flush();

        product.setPrice(159.00);
        productDao.update(product);
        productDao.flush();

        assertEquals(159.00, productDao.findById(product.getId()).getPrice(), 0.001);
    }

    @Test
    @DisplayName("delete supprime une entite")
    void deleteRemovesEntity() {
        Product product = new Product("Webcam HD", 79.00);
        productDao.save(product);
        productDao.flush();
        Long id = product.getId();

        productDao.delete(product);
        productDao.flush();

        assertTrue(productDao.findById(id) == null);
        assertEquals(0L, productDao.count());
    }

    @Test
    @DisplayName("recherche par fragment de nom")
    void findByNameContaining() {
        productDao.save(new Product("Ordinateur portable", 1299.99));
        productDao.save(new Product("Souris sans fil", 24.50));
        productDao.flush();

        List<Product> results = productDao.findByNameContaining("portable");
        assertEquals(1, results.size());
        assertEquals("Ordinateur portable", results.get(0).getName());
    }
}
