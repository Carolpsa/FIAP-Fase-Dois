package br.com.fiap.techchallenge.infrastructure.web.dto;

import br.com.fiap.techchallenge.application.dto.UsuarioInput;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioRequest(
        @Schema(example = "Maria Souza")
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @Schema(example = "maria@email.com")
        @NotBlank(message = "O email é obrigatório")
        @Email(message = "O email informado é inválido")
        String email,

        @Schema(example = "maria.souza")
        @NotBlank(message = "O login é obrigatório")
        String login,

        @Schema(example = "1")
        @NotNull(message = "O tipo de usuário é obrigatório")
        Long tipoUsuarioId) {

    public UsuarioInput toInput() {
        return new UsuarioInput(nome, email, login, tipoUsuarioId);
    }
}
