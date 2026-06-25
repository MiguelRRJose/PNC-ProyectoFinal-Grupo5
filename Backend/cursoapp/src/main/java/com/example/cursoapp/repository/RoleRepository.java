package com.example.cursoapp.repository;

import com.example.cursoapp.domain.entity.Role;
import com.example.cursoapp.domain.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(RoleType roleName);
}