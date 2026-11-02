package br.com.fiap.techchallenge.infrastructure.web.controller;

import br.com.fiap.techchallenge.application.usecase.TipoUsuarioUseCase;
import br.com.fiap.techchallenge.domain.entities.TipoUsuario;
import br.com.fiap.techchallenge.infrastructure.web.dto.TipoUsuarioRequest;
import br.com.fiap.techchallenge.infrastructure.web.dto.TipoUsuarioResponse;
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
@RequestMapping("/tipos-usuario")
@Tag(name = "Tipos de Usuário", description = "CRUD dos tipos de usuário (ex: Dono de Restaurante, Cliente)")
public class TipoUsuarioController {

    private final TipoUsuarioUseCase tipoUsuarioUseCase;

    public TipoUsuarioController(TipoUsuarioUseCase tipoUsuarioUseCase) {
        this.tipoUsuarioUseCase = tipoUsuarioUseCase;
    }

    @PostMapping
    @Operation(summary = "Cadastrar um novo tipo de usuário")
    public ResponseEntity<TipoUsuarioResponse> criar(@Valid @RequestBody TipoUsuarioRequest request) {
        TipoUsuario criado = tipoUsuarioUseCase.criar(request.nome());
        return ResponseEntity.status(HttpStatus.CREATED).body(TipoUsuarioResponse.fromDomain(criado));
    }

    @GetMapping
    @Operation(summary = "Listar todos os tipos de usuário")
    public ResponseEntity<List<TipoUsuarioResponse>> listarTodos() {
        List<TipoUsuarioResponse> resposta = tipoUsuarioUseCase.listarTodos().stream()
                .map(TipoUsuarioResponse::fromDomain)
                .toList();
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um tipo de usuário pelo id")
    public ResponseEntity<TipoUsuarioResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(TipoUsuarioResponse.fromDomain(tipoUsuarioUseCase.buscarPorId(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um tipo de usuário")
    public ResponseEntity<TipoUsuarioResponse> atualizar(@PathVariable Long id,
                                                         @Valid @RequestBody TipoUsuarioRequest request) {
        TipoUsuario atualizado = tipoUsuarioUseCase.atualizar(id, request.nome());
        return ResponseEntity.ok(TipoUsuarioResponse.fromDomain(atualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um tipo de usuário (só se nenhum usuário estiver usando)")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        tipoUsuarioUseCase.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
