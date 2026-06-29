package com.example.cursoapp.repository.identity;

import com.example.cursoapp.domain.entity.identity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByUsernameOrEmail(String username, String email);
    List<Usuario> findByRoleId(Long roleId);
    List<Usuario> findByIsActive(Boolean isActive);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
