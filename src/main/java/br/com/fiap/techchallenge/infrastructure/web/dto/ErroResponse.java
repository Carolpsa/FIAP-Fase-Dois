package br.com.fiap.techchallenge.infrastructure.web.dto;

import java.time.LocalDateTime;
import java.util.List;


public record ErroResponse(LocalDateTime dataHora, int status, String erro, String mensagem, List<String> detalhes) {
}
