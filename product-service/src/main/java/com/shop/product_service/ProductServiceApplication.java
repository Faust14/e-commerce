package com.shop.product_service;

import com.shop.common.config.SecurityConfig;
import com.shop.product_service.domain.Category;
import com.shop.product_service.domain.Product;
import com.shop.product_service.repository.CategoryRepository;
import com.shop.product_service.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import({
		SecurityConfig.class
})
@SpringBootApplication
public class ProductServiceApplication {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CategoryRepository categoryRepository;

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}

	@PostConstruct
	public void createProduct() {
		Category category = Category.builder()
				.name("Electronics")
				.build();
		Category homeCategory = Category.builder()
				.name("Home Appliances")
				.build();
		Category phoneCategory = Category.builder()
				.name("Smartphones")
				.build();
		categoryRepository.saveAndFlush(category);
		categoryRepository.saveAndFlush(homeCategory);
		categoryRepository.saveAndFlush(phoneCategory);

		Product product = Product.builder()
				.name("Laptop Lenovo")
				.description("High-performance laptop with 16GB RAM and SSD storage.")
				.price(1200.00)
				.quantity(1)
				.category(category)
				.build();

		Product product1 = Product.builder()
				.name("Samsung Galaxy S23")
				.description("Flagship smartphone with a 120Hz AMOLED display.")
				.price(999.99)
				.quantity(10)
				.category(phoneCategory)
				.build();

		Product product2 = Product.builder()
				.name("iPhone 14 Pro")
				.description("Apple’s latest smartphone with a 48MP camera.")
				.price(1299.99)
				.quantity(10)
				.category(phoneCategory)
				.build();

		List<Product> products = List.of(
				Product.builder()
						.name("Washing Machine")
						.description("Automatic washing machine with inverter technology.")
						.price(600.00)
						.category(category)
						.build(),

				Product.builder()
						.name("Microwave Oven")
						.description("800W microwave with grill function.")
						.price(150.00)
						.category(category)
						.build()
		);
		productRepository.saveAndFlush(product);
		productRepository.saveAndFlush(product2);
		productRepository.saveAndFlush(product1);
	}

}
