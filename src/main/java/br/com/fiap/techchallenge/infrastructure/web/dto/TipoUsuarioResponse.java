package br.com.fiap.techchallenge.infrastructure.web.dto;

import br.com.fiap.techchallenge.domain.entities.TipoUsuario;

public record TipoUsuarioResponse(Long id, String nome) {

    public static TipoUsuarioResponse fromDomain(TipoUsuario tipoUsuario) {
        return new TipoUsuarioResponse(tipoUsuario.getId(), tipoUsuario.getNome());
    }
}
