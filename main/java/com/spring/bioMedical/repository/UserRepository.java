package com.spring.bioMedical.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.spring.bioMedical.entity.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {


    User findByEmail(String email);


    User findByConfirmationToken(String confirmationToken);

    List<User> findAll();

    boolean existsByEmail(String email);
}