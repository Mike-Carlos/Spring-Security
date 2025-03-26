package com.activity.secure.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.activity.secure.Model.Inventory;

/**
 * This interface represents a JPA repository for managing inventory items.
 * It extends the JpaRepository interface provided by Spring Data JPA.
 */
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    /**
     * This method retrieves a list of inventory items based on the specified category.
     *
     * @param category the category to search for
     * @return a list of inventory items with the specified category
     */
    List<Inventory> findByCategory(String category);

    /**
     * This method retrieves a list of inventory items whose names contain the specified string,
     * ignoring case sensitivity.
     *
     * @param name the string to search for in the item names
     * @return a list of inventory items whose names contain the specified string
     */
    List<Inventory> findByNameContainingIgnoreCase(String name);
}