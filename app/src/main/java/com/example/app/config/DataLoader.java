package com.example.app.config;

import com.example.core.entity.Item;
import com.example.core.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
public class DataLoader {

    private final ItemService itemService;

    @Bean
    @Profile("dev")
    public CommandLineRunner loadData() {
        return args -> {
            // Check if we already have data
            if (itemService.getAllItems().isEmpty()) {
                System.out.println("Loading sample data...");
                
                itemService.saveItem(new Item(null, "Laptop", "High-performance laptop", 1299.99));
                itemService.saveItem(new Item(null, "Smartphone", "Latest model smartphone", 899.99));
                itemService.saveItem(new Item(null, "Headphones", "Noise-cancelling headphones", 249.99));
                itemService.saveItem(new Item(null, "Tablet", "10-inch tablet", 499.99));
                itemService.saveItem(new Item(null, "Smartwatch", "Fitness tracking smartwatch", 199.99));
                
                System.out.println("Sample data loaded successfully!");
            } else {
                System.out.println("Database already contains data, skipping data load.");
            }
        };
    }
}