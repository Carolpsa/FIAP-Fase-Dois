package br.com.fiap.techchallenge.infrastructure.persistence.mapper;

import br.com.fiap.techchallenge.domain.entities.TipoUsuario;
import br.com.fiap.techchallenge.infrastructure.persistence.entity.TipoUsuarioEntity;


public class TipoUsuarioMapper {

    public static TipoUsuario toDomain(TipoUsuarioEntity entity) {
        return new TipoUsuario(entity.getId(), entity.getNome());
    }

    public static TipoUsuarioEntity toEntity(TipoUsuario tipoUsuario) {
        return new TipoUsuarioEntity(tipoUsuario.getId(), tipoUsuario.getNome());
    }
}
