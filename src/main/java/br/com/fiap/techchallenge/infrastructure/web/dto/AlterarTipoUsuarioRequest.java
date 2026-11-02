package br.com.fiap.techchallenge.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;


public record AlterarTipoUsuarioRequest(
        @Schema(example = "1")
        @NotNull(message = "O tipo de usuário é obrigatório")
        Long tipoUsuarioId) {
}
