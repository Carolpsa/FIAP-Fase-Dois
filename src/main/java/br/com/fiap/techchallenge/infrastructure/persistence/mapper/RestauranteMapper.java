package br.com.fiap.techchallenge.infrastructure.persistence.mapper;

import br.com.fiap.techchallenge.domain.entities.Restaurante;
import br.com.fiap.techchallenge.infrastructure.persistence.entity.RestauranteEntity;

public class RestauranteMapper {

    public static Restaurante toDomain(RestauranteEntity entity) {
        return new Restaurante(
                entity.getId(),
                entity.getNome(),
                entity.getEndereco(),
                entity.getTipoCozinha(),
                entity.getHorarioFuncionamento(),
                UsuarioMapper.toDomain(entity.getDono()));
    }

    public static RestauranteEntity toEntity(Restaurante restaurante) {
        return new RestauranteEntity(
                restaurante.getId(),
                restaurante.getNome(),
                restaurante.getEndereco(),
                restaurante.getTipoCozinha(),
                restaurante.getHorarioFuncionamento(),
                UsuarioMapper.toEntity(restaurante.getDono()));
    }
}
