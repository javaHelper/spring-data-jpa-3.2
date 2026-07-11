package com.example.repository;

import com.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    List<User> findByFirstNameIgnoreCase(String firstName);

    List<User> findByDepartment(String department);

    @Query("SELECT u FROM User u WHERE u.address.city = :city")
    List<User> findUsersByCity(@Param("city") String city);

    @Query("SELECT u FROM User u WHERE u.personalContact.email = :email")
    Optional<User> findByPersonalEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.workContact.email = :email")
    Optional<User> findByWorkEmail(@Param("email") String email);
}
