package com.example.core.service;

import com.example.core.entity.Item;
import com.example.core.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    private Item item1;
    private Item item2;

    @BeforeEach
    void setUp() {
        item1 = new Item(1L, "Test Item 1", "Description 1", 10.0);
        item2 = new Item(2L, "Test Item 2", "Description 2", 20.0);
    }

    @Test
    void getAllItems_ShouldReturnAllItems() {
        // Arrange
        when(itemRepository.findAll()).thenReturn(Arrays.asList(item1, item2));

        // Act
        List<Item> result = itemService.getAllItems();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Test Item 1", result.get(0).getName());
        assertEquals("Test Item 2", result.get(1).getName());
        verify(itemRepository, times(1)).findAll();
    }

    @Test
    void getItemById_WithExistingId_ShouldReturnItem() {
        // Arrange
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));

        // Act
        Optional<Item> result = itemService.getItemById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Test Item 1", result.get().getName());
        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    void getItemById_WithNonExistingId_ShouldReturnEmpty() {
        // Arrange
        when(itemRepository.findById(3L)).thenReturn(Optional.empty());

        // Act
        Optional<Item> result = itemService.getItemById(3L);

        // Assert
        assertFalse(result.isPresent());
        verify(itemRepository, times(1)).findById(3L);
    }

    @Test
    void saveItem_ShouldReturnSavedItem() {
        // Arrange
        Item newItem = new Item(null, "New Item", "New Description", 30.0);
        Item savedItem = new Item(3L, "New Item", "New Description", 30.0);
        when(itemRepository.save(any(Item.class))).thenReturn(savedItem);

        // Act
        Item result = itemService.saveItem(newItem);

        // Assert
        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("New Item", result.getName());
        verify(itemRepository, times(1)).save(newItem);
    }

    @Test
    void deleteItem_ShouldCallRepositoryDeleteById() {
        // Arrange
        doNothing().when(itemRepository).deleteById(1L);

        // Act
        itemService.deleteItem(1L);

        // Assert
        verify(itemRepository, times(1)).deleteById(1L);
    }

    @Test
    void searchItemsByName_ShouldReturnMatchingItems() {
        // Arrange
        when(itemRepository.findByNameContaining("Test")).thenReturn(Arrays.asList(item1, item2));

        // Act
        List<Item> result = itemService.searchItemsByName("Test");

        // Assert
        assertEquals(2, result.size());
        verify(itemRepository, times(1)).findByNameContaining("Test");
    }

    @Test
    void findItemsUnderPrice_ShouldReturnItemsUnderPrice() {
        // Arrange
        when(itemRepository.findByPriceLessThanEqual(15.0)).thenReturn(Arrays.asList(item1));

        // Act
        List<Item> result = itemService.findItemsUnderPrice(15.0);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Test Item 1", result.get(0).getName());
        verify(itemRepository, times(1)).findByPriceLessThanEqual(15.0);
    }
}