package br.com.fiap.techchallenge.infrastructure.web.controller;

import br.com.fiap.techchallenge.application.usecase.ItemCardapioUseCase;
import br.com.fiap.techchallenge.domain.entities.ItemCardapio;
import br.com.fiap.techchallenge.infrastructure.web.dto.ItemCardapioRequest;
import br.com.fiap.techchallenge.infrastructure.web.dto.ItemCardapioResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/itens-cardapio")
@Tag(name = "Itens do Cardápio", description = "CRUD dos itens vendidos nos restaurantes")
public class ItemCardapioController {

    private final ItemCardapioUseCase itemCardapioUseCase;

    public ItemCardapioController(ItemCardapioUseCase itemCardapioUseCase) {
        this.itemCardapioUseCase = itemCardapioUseCase;
    }

    @PostMapping
    @Operation(summary = "Cadastrar um item no cardápio de um restaurante")
    public ResponseEntity<ItemCardapioResponse> criar(@Valid @RequestBody ItemCardapioRequest request) {
        ItemCardapio criado = itemCardapioUseCase.criar(request.toInput());
        return ResponseEntity.status(HttpStatus.CREATED).body(ItemCardapioResponse.fromDomain(criado));
    }

    @GetMapping
    @Operation(summary = "Listar itens do cardápio (opcionalmente filtrando por restaurante)")
    public ResponseEntity<List<ItemCardapioResponse>> listar(@RequestParam(required = false) Long restauranteId) {
        List<ItemCardapio> itens;
        if (restauranteId != null) {
            itens = itemCardapioUseCase.listarPorRestaurante(restauranteId);
        } else {
            itens = itemCardapioUseCase.listarTodos();
        }
        return ResponseEntity.ok(itens.stream().map(ItemCardapioResponse::fromDomain).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um item do cardápio pelo id")
    public ResponseEntity<ItemCardapioResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ItemCardapioResponse.fromDomain(itemCardapioUseCase.buscarPorId(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um item do cardápio")
    public ResponseEntity<ItemCardapioResponse> atualizar(@PathVariable Long id,
                                                          @Valid @RequestBody ItemCardapioRequest request) {
        ItemCardapio atualizado = itemCardapioUseCase.atualizar(id, request.toInput());
        return ResponseEntity.ok(ItemCardapioResponse.fromDomain(atualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um item do cardápio")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        itemCardapioUseCase.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
