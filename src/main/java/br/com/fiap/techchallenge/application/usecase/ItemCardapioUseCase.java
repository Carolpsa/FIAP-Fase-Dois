package br.com.fiap.techchallenge.application.usecase;

import br.com.fiap.techchallenge.application.dto.ItemCardapioInput;
import br.com.fiap.techchallenge.domain.entities.ItemCardapio;
import br.com.fiap.techchallenge.domain.entities.Restaurante;
import br.com.fiap.techchallenge.domain.exception.NaoEncontradoException;
import br.com.fiap.techchallenge.domain.exception.ValidacaoException;
import br.com.fiap.techchallenge.domain.gateway.ItemCardapioGateway;
import br.com.fiap.techchallenge.domain.gateway.RestauranteGateway;

import java.util.List;


public class ItemCardapioUseCase {

    private final ItemCardapioGateway itemCardapioGateway;
    private final RestauranteGateway restauranteGateway;

    public ItemCardapioUseCase(ItemCardapioGateway itemCardapioGateway, RestauranteGateway restauranteGateway) {
        this.itemCardapioGateway = itemCardapioGateway;
        this.restauranteGateway = restauranteGateway;
    }

    public ItemCardapio criar(ItemCardapioInput dados) {
        Restaurante restaurante = buscarRestaurante(dados.restauranteId());
        ItemCardapio novoItem = new ItemCardapio(null, dados.nome(), dados.descricao(), dados.preco(),
                dados.disponivelApenasNoLocal(), dados.caminhoFoto(), restaurante);
        return itemCardapioGateway.salvar(novoItem);
    }

    public ItemCardapio buscarPorId(Long id) {
        return itemCardapioGateway.buscarPorId(id)
                .orElseThrow(() -> new NaoEncontradoException("Item do cardápio não encontrado com o id " + id));
    }

    public List<ItemCardapio> listarTodos() {
        return itemCardapioGateway.listarTodos();
    }

    public List<ItemCardapio> listarPorRestaurante(Long restauranteId) {
        buscarRestaurante(restauranteId); 
        return itemCardapioGateway.listarPorRestaurante(restauranteId);
    }

    public ItemCardapio atualizar(Long id, ItemCardapioInput dados) {
        buscarPorId(id);
        Restaurante restaurante = buscarRestaurante(dados.restauranteId());
        ItemCardapio itemAtualizado = new ItemCardapio(id, dados.nome(), dados.descricao(), dados.preco(),
                dados.disponivelApenasNoLocal(), dados.caminhoFoto(), restaurante);
        return itemCardapioGateway.salvar(itemAtualizado);
    }

    public void deletar(Long id) {
        buscarPorId(id);
        itemCardapioGateway.deletar(id);
    }

    private Restaurante buscarRestaurante(Long restauranteId) {
        if (restauranteId == null) {
            throw new ValidacaoException("O campo restaurante é obrigatório");
        }
        return restauranteGateway.buscarPorId(restauranteId)
                .orElseThrow(() -> new NaoEncontradoException("Restaurante não encontrado com o id " + restauranteId));
    }
}
