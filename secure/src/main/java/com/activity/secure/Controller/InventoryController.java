package com.activity.secure.Controller;

import com.activity.secure.Model.Inventory;
import com.activity.secure.Service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@Tag(name = "Inventory Management", description = "CRUD operations for inventory items")
@SecurityRequirement(name = "bearerAuth") // Requires JWT token for all endpoints
public class InventoryController {

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Operation(summary = "Get all inventory items", description = "Retrieves a list of all inventory items")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Inventory.class))),
        @ApiResponse(responseCode = "204", description = "No content found"),
        @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
    })
    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventory() {
        List<Inventory> inventory = inventoryService.getAllInventory();
        if (inventory.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(inventory);
    }

    @Operation(summary = "Get inventory item by ID", description = "Returns a single inventory item by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved item",
            content = @Content(schema = @Schema(implementation = Inventory.class))),
        @ApiResponse(responseCode = "404", description = "Item not found",
            content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid ID supplied",
            content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventoryById(
            @Parameter(description = "ID of item to be retrieved", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(inventoryService.getInventoryById(id));
    }

    @Operation(summary = "Get inventory items by category", description = "Returns items matching the specified category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved items",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Inventory.class))),
        @ApiResponse(responseCode = "204", description = "No items found for this category"),
        @ApiResponse(responseCode = "400", description = "Invalid category parameter")
    })
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Inventory>> getInventoryByCategory(
            @Parameter(description = "Category to filter by", required = true, example = "electronics")
            @PathVariable String category) {
        List<Inventory> inventory = inventoryService.getInventoryByCategory(category);
        if (inventory.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(inventory);
    }

    @Operation(summary = "Search inventory items by name", description = "Returns items containing the search term in their name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved items",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Inventory.class))),
        @ApiResponse(responseCode = "204", description = "No items match the search term"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameter")
    })    
    @GetMapping("/search")
    public ResponseEntity<List<Inventory>> searchInventoryByName(
            @Parameter(description = "Search term for item name", required = true, example = "laptop")
            @RequestParam String name) {
        List<Inventory> inventory = inventoryService.searchInventoryByName(name);
        if (inventory.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(inventory);
    }

    @Operation(summary = "Create a new inventory item", description = "Adds a new item to the inventory")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Item created successfully",
            content = @Content(schema = @Schema(implementation = Inventory.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input",
            content = @Content),
        @ApiResponse(responseCode = "409", description = "Item already exists")
    })    
    @PostMapping
    public ResponseEntity<Inventory> createInventory(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Inventory item to create",
                required = true,
                content = @Content(
                    schema = @Schema(implementation = Inventory.class),
                    examples = @ExampleObject(
                        value = "{\"name\": \"New Item\", \"description\": \"For photography\", \"category\": \"electronics\", \"quantity\": 10, \"price\": 99.99}"
                    )
                )
            )
            @Valid @RequestBody Inventory inventory) {
        Inventory createdItem = inventoryService.createInventory(inventory);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdItem.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdItem);
    }

    @Operation(summary = "Update an existing inventory item", description = "Updates details of an existing item")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item updated successfully",
            content = @Content(schema = @Schema(implementation = Inventory.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Item not found")
    })    
    @PutMapping("/{id}")
    public ResponseEntity<Inventory> updateInventory(
        @Parameter(description = "ID of item to be updated", required = true, example = "1")
        @PathVariable Long id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Updated inventory item data",
            required = true,
            content = @Content(
                schema = @Schema(implementation = Inventory.class),
                examples = @ExampleObject(
                    value = "{\"name\": \"Updated Item\", \"quantity\": 15}"
                )
            )
        )
        @Valid @RequestBody Inventory inventoryDetails) {
        Inventory updatedItem = inventoryService.updateInventory(id, inventoryDetails);
        return ResponseEntity.ok(updatedItem);
    }

    @Operation(summary = "Delete an inventory item", description = "Removes an item from the inventory")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Item deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Item not found"),
        @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
    })    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(
            @Parameter(description = "ID of item to be deleted", required = true, example = "1")
            @PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }
}