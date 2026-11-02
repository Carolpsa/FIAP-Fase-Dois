package br.com.fiap.techchallenge.application.dto;


public record RestauranteInput(String nome, String endereco, String tipoCozinha,
                               String horarioFuncionamento, Long donoId) {
}
