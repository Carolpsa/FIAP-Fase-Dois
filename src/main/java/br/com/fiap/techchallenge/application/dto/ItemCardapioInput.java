package br.com.fiap.techchallenge.application.dto;

import java.math.BigDecimal;


public record ItemCardapioInput(String nome, String descricao, BigDecimal preco,
                                boolean disponivelApenasNoLocal, String caminhoFoto, Long restauranteId) {
}
