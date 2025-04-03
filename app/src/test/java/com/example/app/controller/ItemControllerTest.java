package com.example.app.controller;

import com.example.core.entity.Item;
import com.example.core.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    private Item item1;
    private Item item2;

    @BeforeEach
    void setUp() {
        item1 = new Item(1L, "Test Item 1", "Description 1", 10.0);
        item2 = new Item(2L, "Test Item 2", "Description 2", 20.0);
    }

    @Test
    void getAllItems_ShouldReturnAllItems() throws Exception {
        // Arrange
        when(itemService.getAllItems()).thenReturn(Arrays.asList(item1, item2));

        // Act & Assert
        mockMvc.perform(get("/api/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Test Item 1")))
                .andExpect(jsonPath("$[1].name", is("Test Item 2")));

        verify(itemService, times(1)).getAllItems();
    }

    @Test
    void getItemById_WithExistingId_ShouldReturnItem() throws Exception {
        // Arrange
        when(itemService.getItemById(1L)).thenReturn(Optional.of(item1));

        // Act & Assert
        mockMvc.perform(get("/api/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test Item 1")))
                .andExpect(jsonPath("$.description", is("Description 1")))
                .andExpect(jsonPath("$.price", is(10.0)));

        verify(itemService, times(1)).getItemById(1L);
    }

    @Test
    void getItemById_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(itemService.getItemById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/items/99"))
                .andExpect(status().isNotFound());

        verify(itemService, times(1)).getItemById(99L);
    }

    @Test
    void createItem_ShouldReturnCreatedItem() throws Exception {
        // Arrange
        Item newItem = new Item(null, "New Item", "New Description", 30.0);
        Item savedItem = new Item(3L, "New Item", "New Description", 30.0);

        when(itemService.saveItem(any(Item.class))).thenReturn(savedItem);

        // Act & Assert
        mockMvc.perform(post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newItem)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.name", is("New Item")))
                .andExpect(jsonPath("$.description", is("New Description")))
                .andExpect(jsonPath("$.price", is(30.0)));

        verify(itemService, times(1)).saveItem(any(Item.class));
    }

    @Test
    void updateItem_WithExistingId_ShouldReturnUpdatedItem() throws Exception {
        // Arrange
        Item updatedItem = new Item(1L, "Updated Item", "Updated Description", 15.0);

        when(itemService.getItemById(1L)).thenReturn(Optional.of(item1));
        when(itemService.saveItem(any(Item.class))).thenReturn(updatedItem);

        // Act & Assert
        mockMvc.perform(put("/api/items/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Updated Item")))
                .andExpect(jsonPath("$.description", is("Updated Description")))
                .andExpect(jsonPath("$.price", is(15.0)));

        verify(itemService, times(1)).getItemById(1L);
        verify(itemService, times(1)).saveItem(any(Item.class));
    }

    @Test
    void updateItem_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        // Arrange
        Item updatedItem = new Item(99L, "Updated Item", "Updated Description", 15.0);

        when(itemService.getItemById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(put("/api/items/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedItem)))
                .andExpect(status().isNotFound());

        verify(itemService, times(1)).getItemById(99L);
        verify(itemService, never()).saveItem(any(Item.class));
    }

    @Test
    void deleteItem_WithExistingId_ShouldReturnNoContent() throws Exception {
        // Arrange
        when(itemService.getItemById(1L)).thenReturn(Optional.of(item1));
        doNothing().when(itemService).deleteItem(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/items/1"))
                .andExpect(status().isNoContent());

        verify(itemService, times(1)).getItemById(1L);
        verify(itemService, times(1)).deleteItem(1L);
    }

    @Test
    void deleteItem_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(itemService.getItemById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(delete("/api/items/99"))
                .andExpect(status().isNotFound());

        verify(itemService, times(1)).getItemById(99L);
        verify(itemService, never()).deleteItem(anyLong());
    }
}