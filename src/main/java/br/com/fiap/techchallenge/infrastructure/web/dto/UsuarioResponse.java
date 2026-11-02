package br.com.fiap.techchallenge.infrastructure.web.dto;

import br.com.fiap.techchallenge.domain.entities.Usuario;

public record UsuarioResponse(Long id, String nome, String email, String login, TipoUsuarioResponse tipoUsuario) {

    public static UsuarioResponse fromDomain(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getLogin(),
                TipoUsuarioResponse.fromDomain(usuario.getTipoUsuario()));
    }
}
