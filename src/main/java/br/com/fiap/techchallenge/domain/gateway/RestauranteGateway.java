package br.com.fiap.techchallenge.domain.gateway;

import java.util.List;
import java.util.Optional;

import br.com.fiap.techchallenge.domain.entities.Restaurante;

public interface RestauranteGateway {

    Restaurante salvar(Restaurante restaurante);

    Optional<Restaurante> buscarPorId(Long id);

    List<Restaurante> listarTodos();

    void deletar(Long id);

    boolean existeRestauranteDoDono(Long donoId);
}
