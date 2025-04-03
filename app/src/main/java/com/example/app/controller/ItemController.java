package com.example.app.controller;

import com.example.core.entity.Item;
import com.example.core.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    
    @GetMapping
    public ResponseEntity<List<Item>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        return itemService.getItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Item>> searchItems(@RequestParam String name) {
        return ResponseEntity.ok(itemService.searchItemsByName(name));
    }
    
    @GetMapping("/price")
    public ResponseEntity<List<Item>> findItemsUnderPrice(@RequestParam Double maxPrice) {
        return ResponseEntity.ok(itemService.findItemsUnderPrice(maxPrice));
    }
    
    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.saveItem(item));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item item) {
        return itemService.getItemById(id)
                .map(existingItem -> {
                    item.setId(id);
                    return ResponseEntity.ok(itemService.saveItem(item));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        if (itemService.getItemById(id).isPresent()) {
            itemService.deleteItem(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}