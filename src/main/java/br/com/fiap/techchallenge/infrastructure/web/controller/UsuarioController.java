package br.com.fiap.techchallenge.infrastructure.web.controller;

import br.com.fiap.techchallenge.application.usecase.UsuarioUseCase;
import br.com.fiap.techchallenge.domain.entities.Usuario;
import br.com.fiap.techchallenge.infrastructure.web.dto.AlterarTipoUsuarioRequest;
import br.com.fiap.techchallenge.infrastructure.web.dto.UsuarioRequest;
import br.com.fiap.techchallenge.infrastructure.web.dto.UsuarioResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "CRUD de usuários e associação com o tipo de usuário")
public class UsuarioController {

    private final UsuarioUseCase usuarioUseCase;

    public UsuarioController(UsuarioUseCase usuarioUseCase) {
        this.usuarioUseCase = usuarioUseCase;
    }

    @PostMapping
    @Operation(summary = "Cadastrar um novo usuário já associado a um tipo")
    public ResponseEntity<UsuarioResponse> criar(@Valid @RequestBody UsuarioRequest request) {
        Usuario criado = usuarioUseCase.criar(request.toInput());
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponse.fromDomain(criado));
    }

    @GetMapping
    @Operation(summary = "Listar todos os usuários")
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        List<UsuarioResponse> resposta = usuarioUseCase.listarTodos().stream()
                .map(UsuarioResponse::fromDomain)
                .toList();
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um usuário pelo id")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(UsuarioResponse.fromDomain(usuarioUseCase.buscarPorId(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar os dados de um usuário")
    public ResponseEntity<UsuarioResponse> atualizar(@PathVariable Long id,
                                                     @Valid @RequestBody UsuarioRequest request) {
        Usuario atualizado = usuarioUseCase.atualizar(id, request.toInput());
        return ResponseEntity.ok(UsuarioResponse.fromDomain(atualizado));
    }

    @PatchMapping("/{id}/tipo")
    @Operation(summary = "Associar um usuário existente a um tipo de usuário")
    public ResponseEntity<UsuarioResponse> alterarTipo(@PathVariable Long id,
                                                       @Valid @RequestBody AlterarTipoUsuarioRequest request) {
        Usuario atualizado = usuarioUseCase.alterarTipo(id, request.tipoUsuarioId());
        return ResponseEntity.ok(UsuarioResponse.fromDomain(atualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um usuário (só se ele não for dono de restaurante)")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioUseCase.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
