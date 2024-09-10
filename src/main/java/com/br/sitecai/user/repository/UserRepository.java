package com.br.sitecai.user.repository;

import com.br.sitecai.user.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {

    boolean existsByUsername(String username);

    Optional<Usuario> findByUsername(String username);
}
