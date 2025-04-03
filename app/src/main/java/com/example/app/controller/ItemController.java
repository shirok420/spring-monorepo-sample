package com.example.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.core.entity.Item;
import com.example.core.service.ItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
@Tag(name = "Item API", description = "Operations for managing items")
public class ItemController {

    private final ItemService itemService;

    @Operation(summary = "Get all items", description = "Retrieves a list of all items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved items", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Item.class)))
    })
    @GetMapping
    public ResponseEntity<List<Item>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    @Operation(summary = "Get item by ID", description = "Retrieves an item by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the item", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Item.class))),
            @ApiResponse(responseCode = "404", description = "Item not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(
            @Parameter(description = "ID of the item to retrieve", required = true) @PathVariable Long id) {
        return itemService.getItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Search items by name", description = "Retrieves items containing the specified name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved items", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Item.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<List<Item>> searchItems(
            @Parameter(description = "Name to search for", required = true) @RequestParam String name) {
        return ResponseEntity.ok(itemService.searchItemsByName(name));
    }

    @Operation(summary = "Find items under price", description = "Retrieves items with price less than or equal to the specified value")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved items", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Item.class)))
    })
    @GetMapping("/price")
    public ResponseEntity<List<Item>> findItemsUnderPrice(
            @Parameter(description = "Maximum price", required = true) @RequestParam Double maxPrice) {
        return ResponseEntity.ok(itemService.findItemsUnderPrice(maxPrice));
    }

    @Operation(summary = "Create a new item", description = "Creates a new item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item successfully created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Item.class)))
    })
    @PostMapping
    public ResponseEntity<Item> createItem(
            @Parameter(description = "Item to create", required = true) @RequestBody Item item) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.saveItem(item));
    }

    @Operation(summary = "Update an existing item", description = "Updates an item by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item successfully updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Item.class))),
            @ApiResponse(responseCode = "404", description = "Item not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(
            @Parameter(description = "ID of the item to update", required = true) @PathVariable Long id,
            @Parameter(description = "Updated item details", required = true) @RequestBody Item item) {
        return itemService.getItemById(id)
                .map(existingItem -> {
                    item.setId(id);
                    return ResponseEntity.ok(itemService.saveItem(item));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete an item", description = "Deletes an item by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Item not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(
            @Parameter(description = "ID of the item to delete", required = true) @PathVariable Long id) {
        if (itemService.getItemById(id).isPresent()) {
            itemService.deleteItem(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}