package com.eyepax.authservice.repository;

import com.eyepax.authservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByCognitoSub(String cognitoSub);
    Optional<User> findByEmail(String email);
}