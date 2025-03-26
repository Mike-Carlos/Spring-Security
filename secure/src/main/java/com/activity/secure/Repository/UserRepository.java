package com.activity.secure.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.activity.secure.Model.User;
import java.util.Optional;

/**
 * This interface represents a JPA repository for managing users.
 * It extends the JpaRepository interface provided by Spring Data JPA.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * This method retrieves an optional user based on the specified username.
     * If a user with the given username exists, it will be returned wrapped in an Optional.
     * If no user with the given username exists, an empty Optional will be returned.
     *
     * @param username the username to search for
     * @return an optional user with the specified username
     */
    Optional<User> findByUsername(String username);
}