package br.com.fiap.techchallenge.infrastructure.web.dto;

import br.com.fiap.techchallenge.application.dto.ItemCardapioInput;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ItemCardapioRequest(
        @Schema(example = "Lasanha à bolonhesa")
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @Schema(example = "Lasanha caseira com molho bolonhesa e queijo gratinado")
        @NotBlank(message = "A descrição é obrigatória")
        String descricao,

        @Schema(example = "49.90")
        @NotNull(message = "O preço é obrigatório")
        @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
        BigDecimal preco,

        @Schema(example = "false")
        @NotNull(message = "Informe se o item é disponível apenas no local")
        Boolean disponivelApenasNoLocal,

        @Schema(example = "/imagens/pratos/lasanha.jpg")
        String caminhoFoto,

        @Schema(example = "1")
        @NotNull(message = "O restaurante é obrigatório")
        Long restauranteId) {

    public ItemCardapioInput toInput() {
        return new ItemCardapioInput(nome, descricao, preco, disponivelApenasNoLocal, caminhoFoto, restauranteId);
    }
}
