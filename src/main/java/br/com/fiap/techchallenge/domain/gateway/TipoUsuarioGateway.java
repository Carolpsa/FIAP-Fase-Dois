package br.com.fiap.techchallenge.domain.gateway;

import java.util.List;
import java.util.Optional;

import br.com.fiap.techchallenge.domain.entities.TipoUsuario;


public interface TipoUsuarioGateway {

    TipoUsuario salvar(TipoUsuario tipoUsuario);

    Optional<TipoUsuario> buscarPorId(Long id);

    Optional<TipoUsuario> buscarPorNome(String nome);

    List<TipoUsuario> listarTodos();

    void deletar(Long id);
}
