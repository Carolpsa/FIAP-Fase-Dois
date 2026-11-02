package br.com.fiap.techchallenge.infrastructure.persistence.mapper;

import br.com.fiap.techchallenge.domain.entities.Usuario;
import br.com.fiap.techchallenge.infrastructure.persistence.entity.UsuarioEntity;

public class UsuarioMapper {

    public static Usuario toDomain(UsuarioEntity entity) {
        return new Usuario(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getLogin(),
                TipoUsuarioMapper.toDomain(entity.getTipoUsuario()));
    }

    public static UsuarioEntity toEntity(Usuario usuario) {
        return new UsuarioEntity(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getLogin(),
                TipoUsuarioMapper.toEntity(usuario.getTipoUsuario()));
    }
}
