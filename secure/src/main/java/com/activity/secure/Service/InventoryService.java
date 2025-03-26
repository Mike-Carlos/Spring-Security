package com.activity.secure.Service;

import java.util.List;
import com.activity.secure.Model.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.activity.secure.Repository.InventoryRepository;

/**
 * This class provides services for managing inventory items.
 */
@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    /**
     * Constructs an instance of the InventoryService class.
     *
     * @param inventoryRepository the inventory repository
     */
    @Autowired
    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    /**
     * Retrieves all inventory items.
     *
     * @return a list of inventory items
     */
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    /**
     * Retrieves an inventory item by its ID.
     *
     * @param id the ID of the inventory item
     * @return the inventory item
     * @throws RuntimeException if the inventory item is not found
     */
    public Inventory getInventoryById(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory item not found with id: " + id));
    }

    /**
     * Retrieves inventory items by their category.
     *
     * @param category the category of the inventory items
     * @return a list of inventory items
     */
    public List<Inventory> getInventoryByCategory(String category) {
        return inventoryRepository.findByCategory(category);
    }

    /**
     * Searches inventory items by their name.
     *
     * @param name the name of the inventory items
     * @return a list of inventory items
     */
    public List<Inventory> searchInventoryByName(String name) {
        return inventoryRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Creates a new inventory item.
     *
     * @param inventory the inventory item to create
     * @return the created inventory item
     */
    public Inventory createInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    /**
     * Updates an existing inventory item.
     *
     * @param id the ID of the inventory item to update
     * @param inventoryDetails the updated inventory item details
     * @return the updated inventory item
     */
    public Inventory updateInventory(Long id, Inventory inventoryDetails) {
        Inventory inventory = getInventoryById(id);
        inventory.setName(inventoryDetails.getName());
        inventory.setDescription(inventoryDetails.getDescription());
        inventory.setQuantity(inventoryDetails.getQuantity());
        inventory.setPrice(inventoryDetails.getPrice());
        inventory.setCategory(inventoryDetails.getCategory());
        return inventoryRepository.save(inventory);
    }

    /**
     * Deletes an inventory item by its ID.
     *
     * @param id the ID of the inventory item to delete
     */
    public void deleteInventory(Long id) {
        Inventory inventory = getInventoryById(id);
        inventoryRepository.delete(inventory);
    }
}