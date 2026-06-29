package com.example.cursoapp.repository.identity;

import com.example.cursoapp.domain.entity.identity.Role;
import com.example.cursoapp.domain.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}