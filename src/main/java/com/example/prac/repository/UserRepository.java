package com.example.prac.repository;

import com.example.prac.data.auth.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long>,
        PagingAndSortingRepository<User, Long> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
}