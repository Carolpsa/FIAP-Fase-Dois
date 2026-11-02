package br.com.fiap.techchallenge.infrastructure.web.dto;

import br.com.fiap.techchallenge.domain.entities.Restaurante;

public record RestauranteResponse(Long id, String nome, String endereco, String tipoCozinha,
                                  String horarioFuncionamento, DonoResponse dono) {

    
    public record DonoResponse(Long id, String nome) {
    }

    public static RestauranteResponse fromDomain(Restaurante restaurante) {
        return new RestauranteResponse(
                restaurante.getId(),
                restaurante.getNome(),
                restaurante.getEndereco(),
                restaurante.getTipoCozinha(),
                restaurante.getHorarioFuncionamento(),
                new DonoResponse(restaurante.getDono().getId(), restaurante.getDono().getNome()));
    }
}
