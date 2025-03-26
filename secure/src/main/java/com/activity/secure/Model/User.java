package com.activity.secure.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * This class represents a user in the application.
 * It is annotated with @Entity to indicate that it is a JPA entity.
 * It is also annotated with @Table to specify the name of the corresponding table in the database.
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;
    private String role; // "USER" or "ADMIN"

    /**
     * Getter for the user's ID.
     *
     * @return the user's ID
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Setter for the user's ID.
     *
     * @param id the user's ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for the user's username.
     *
     * @return the user's username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Setter for the user's username.
     *
     * @param username the user's username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter for the user's password.
     *
     * @return the user's password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Setter for the user's password.
     *
     * @param password the user's password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter for the user's role.
     *
     * @return the user's role ("USER" or "ADMIN")
     */
    public String getRole() {
        return this.role;
    }

    /**
     * Setter for the user's role.
     *
     * @param role the user's role ("USER" or "ADMIN")
     */
    public void setRole(String role) {
        this.role = role;
    }

}