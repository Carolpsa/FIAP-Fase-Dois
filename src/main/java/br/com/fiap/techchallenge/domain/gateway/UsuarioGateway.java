package br.com.fiap.techchallenge.domain.gateway;

import java.util.List;
import java.util.Optional;

import br.com.fiap.techchallenge.domain.entities.Usuario;

public interface UsuarioGateway {

    Usuario salvar(Usuario usuario);

    Optional<Usuario> buscarPorId(Long id);

    Optional<Usuario> buscarPorEmail(String email);

    List<Usuario> listarTodos();

    void deletar(Long id);

    boolean existeUsuarioComTipo(Long tipoUsuarioId);
}
