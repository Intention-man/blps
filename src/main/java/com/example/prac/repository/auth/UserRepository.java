package com.example.prac.repository.auth;

import com.example.prac.model.authEntity.Role;
import com.example.prac.model.authEntity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long>,
        PagingAndSortingRepository<User, Long> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    List<User> findByRole(Role role);

    boolean existsByRole(Role role);

    @Modifying
    @Query("UPDATE User u SET u.role = 'ADMIN' WHERE u.id = :userId")
    void giveAdminRights(Long userId);
}
