package br.com.fiap.techchallenge.application.dto;


public record UsuarioInput(String nome, String email, String login, Long tipoUsuarioId) {
}
