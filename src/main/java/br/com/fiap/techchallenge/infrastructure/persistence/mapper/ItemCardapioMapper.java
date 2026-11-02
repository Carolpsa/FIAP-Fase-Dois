package br.com.fiap.techchallenge.infrastructure.persistence.mapper;

import br.com.fiap.techchallenge.domain.entities.ItemCardapio;
import br.com.fiap.techchallenge.infrastructure.persistence.entity.ItemCardapioEntity;

public class ItemCardapioMapper {

    public static ItemCardapio toDomain(ItemCardapioEntity entity) {
        return new ItemCardapio(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getPreco(),
                entity.isDisponivelApenasNoLocal(),
                entity.getCaminhoFoto(),
                RestauranteMapper.toDomain(entity.getRestaurante()));
    }

    public static ItemCardapioEntity toEntity(ItemCardapio item) {
        return new ItemCardapioEntity(
                item.getId(),
                item.getNome(),
                item.getDescricao(),
                item.getPreco(),
                item.isDisponivelApenasNoLocal(),
                item.getCaminhoFoto(),
                RestauranteMapper.toEntity(item.getRestaurante()));
    }
}
