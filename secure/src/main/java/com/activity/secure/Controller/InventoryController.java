package com.activity.secure.Controller;

import com.activity.secure.Model.Inventory;
import com.activity.secure.Service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "Get all inventory items")
    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    @Operation(summary = "Get inventory item by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryService.getInventoryById(id));
    }

    @Operation(summary = "Get inventory items by category")
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Inventory>> getInventoryByCategory(@PathVariable String category) {
        return ResponseEntity.ok(inventoryService.getInventoryByCategory(category));
    }

    @Operation(summary = "Search inventory items by name")
    @GetMapping("/search")
    public ResponseEntity<List<Inventory>> searchInventoryByName(@RequestParam String name) {
        return ResponseEntity.ok(inventoryService.searchInventoryByName(name));
    }

    @Operation(summary = "Create a new inventory item")
    @PostMapping
    public ResponseEntity<Inventory> createInventory(@RequestBody Inventory inventory) {
        return ResponseEntity.ok(inventoryService.createInventory(inventory));
    }

    @Operation(summary = "Update an existing inventory item")
    @PutMapping("/{id}")
    public ResponseEntity<Inventory> updateInventory(
            @PathVariable Long id,
            @RequestBody Inventory inventoryDetails) {
        return ResponseEntity.ok(inventoryService.updateInventory(id, inventoryDetails));
    }

    @Operation(summary = "Delete an inventory item")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }
}