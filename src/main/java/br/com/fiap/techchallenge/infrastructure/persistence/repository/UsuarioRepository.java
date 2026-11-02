package br.com.fiap.techchallenge.infrastructure.persistence.repository;

import br.com.fiap.techchallenge.infrastructure.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    Optional<UsuarioEntity> findByEmailIgnoreCase(String email);

    boolean existsByTipoUsuario_Id(Long tipoUsuarioId);
}
