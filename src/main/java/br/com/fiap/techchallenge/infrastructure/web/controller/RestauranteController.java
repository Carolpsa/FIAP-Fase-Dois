package br.com.fiap.techchallenge.infrastructure.web.controller;

import br.com.fiap.techchallenge.application.usecase.RestauranteUseCase;
import br.com.fiap.techchallenge.domain.entities.Restaurante;
import br.com.fiap.techchallenge.infrastructure.web.dto.RestauranteRequest;
import br.com.fiap.techchallenge.infrastructure.web.dto.RestauranteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/restaurantes")
@Tag(name = "Restaurantes", description = "CRUD de restaurantes")
public class RestauranteController {

    private final RestauranteUseCase restauranteUseCase;

    public RestauranteController(RestauranteUseCase restauranteUseCase) {
        this.restauranteUseCase = restauranteUseCase;
    }

    @PostMapping
    @Operation(summary = "Cadastrar um restaurante (o dono precisa ser do tipo Dono de Restaurante)")
    public ResponseEntity<RestauranteResponse> criar(@Valid @RequestBody RestauranteRequest request) {
        Restaurante criado = restauranteUseCase.criar(request.toInput());
        return ResponseEntity.status(HttpStatus.CREATED).body(RestauranteResponse.fromDomain(criado));
    }

    @GetMapping
    @Operation(summary = "Listar todos os restaurantes")
    public ResponseEntity<List<RestauranteResponse>> listarTodos() {
        List<RestauranteResponse> resposta = restauranteUseCase.listarTodos().stream()
                .map(RestauranteResponse::fromDomain)
                .toList();
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um restaurante pelo id")
    public ResponseEntity<RestauranteResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(RestauranteResponse.fromDomain(restauranteUseCase.buscarPorId(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um restaurante")
    public ResponseEntity<RestauranteResponse> atualizar(@PathVariable Long id,
                                                         @Valid @RequestBody RestauranteRequest request) {
        Restaurante atualizado = restauranteUseCase.atualizar(id, request.toInput());
        return ResponseEntity.ok(RestauranteResponse.fromDomain(atualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um restaurante (só se ele não tiver itens no cardápio)")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        restauranteUseCase.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
