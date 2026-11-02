package br.com.fiap.techchallenge.infrastructure.persistence.repository;

import br.com.fiap.techchallenge.infrastructure.persistence.entity.TipoUsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoUsuarioRepository extends JpaRepository<TipoUsuarioEntity, Long> {

    Optional<TipoUsuarioEntity> findByNomeIgnoreCase(String nome);
}
