package br.com.fiap.techchallenge.infrastructure.persistence.repository;

import br.com.fiap.techchallenge.infrastructure.persistence.entity.ItemCardapioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemCardapioRepository extends JpaRepository<ItemCardapioEntity, Long> {

    List<ItemCardapioEntity> findByRestaurante_Id(Long restauranteId);

    boolean existsByRestaurante_Id(Long restauranteId);
}
