package br.com.fiap.techchallenge.infrastructure.web.dto;

import br.com.fiap.techchallenge.application.dto.RestauranteInput;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RestauranteRequest(
        @Schema(example = "Cantina da Nona")
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @Schema(example = "Rua das Flores, 123 - Centro, Araraquara/SP")
        @NotBlank(message = "O endereço é obrigatório")
        String endereco,

        @Schema(example = "Italiana")
        @NotBlank(message = "O tipo de cozinha é obrigatório")
        String tipoCozinha,

        @Schema(example = "Terça a Domingo, das 11h às 23h")
        @NotBlank(message = "O horário de funcionamento é obrigatório")
        String horarioFuncionamento,

        @Schema(example = "1")
        @NotNull(message = "O dono do restaurante é obrigatório")
        Long donoId) {

    public RestauranteInput toInput() {
        return new RestauranteInput(nome, endereco, tipoCozinha, horarioFuncionamento, donoId);
    }
}
