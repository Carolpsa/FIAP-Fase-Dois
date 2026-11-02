package br.com.fiap.techchallenge.domain.gateway;

import java.util.List;
import java.util.Optional;

import br.com.fiap.techchallenge.domain.entities.ItemCardapio;

public interface ItemCardapioGateway {

    ItemCardapio salvar(ItemCardapio itemCardapio);

    Optional<ItemCardapio> buscarPorId(Long id);

    List<ItemCardapio> listarTodos();

    List<ItemCardapio> listarPorRestaurante(Long restauranteId);

    void deletar(Long id);

    boolean existeItemNoRestaurante(Long restauranteId);
}
