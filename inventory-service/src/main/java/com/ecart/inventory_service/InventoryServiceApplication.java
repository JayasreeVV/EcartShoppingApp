package com.ecart.inventory_service;

import com.ecart.inventory_service.entity.Inventory;
import com.ecart.inventory_service.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
		return args -> {
			Inventory inventory = new Inventory();
			inventory.setSkuCode("0004");
			inventory.setQuantity(100);
			inventoryRepository.save(inventory);
			Inventory inventory1 = new Inventory();
			inventory1.setSkuCode("0003");
			inventory1.setQuantity(0);
			inventoryRepository.save(inventory1);
		};
	}
}
