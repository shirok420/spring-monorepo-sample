package com.example.core.service;

import com.example.core.entity.Item;
import com.example.core.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }
    
    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }
    
    public List<Item> searchItemsByName(String name) {
        return itemRepository.findByNameContaining(name);
    }
    
    public List<Item> findItemsUnderPrice(Double price) {
        return itemRepository.findByPriceLessThanEqual(price);
    }
    
    @Transactional
    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }
    
    @Transactional
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }
}