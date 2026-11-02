package br.com.fiap.techchallenge.infrastructure.web.dto;

import java.math.BigDecimal;

import br.com.fiap.techchallenge.domain.entities.ItemCardapio;

public record ItemCardapioResponse(Long id, String nome, String descricao, BigDecimal preco,
                                   boolean disponivelApenasNoLocal, String caminhoFoto,
                                   Long restauranteId, String restauranteNome) {

    public static ItemCardapioResponse fromDomain(ItemCardapio item) {
        return new ItemCardapioResponse(
                item.getId(),
                item.getNome(),
                item.getDescricao(),
                item.getPreco(),
                item.isDisponivelApenasNoLocal(),
                item.getCaminhoFoto(),
                item.getRestaurante().getId(),
                item.getRestaurante().getNome());
    }
}
