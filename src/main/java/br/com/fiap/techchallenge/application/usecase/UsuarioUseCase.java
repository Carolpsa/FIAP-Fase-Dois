package br.com.fiap.techchallenge.application.usecase;

import br.com.fiap.techchallenge.application.dto.UsuarioInput;
import br.com.fiap.techchallenge.domain.entities.TipoUsuario;
import br.com.fiap.techchallenge.domain.entities.Usuario;
import br.com.fiap.techchallenge.domain.exception.NaoEncontradoException;
import br.com.fiap.techchallenge.domain.exception.RegraDeNegocioException;
import br.com.fiap.techchallenge.domain.exception.ValidacaoException;
import br.com.fiap.techchallenge.domain.gateway.RestauranteGateway;
import br.com.fiap.techchallenge.domain.gateway.TipoUsuarioGateway;
import br.com.fiap.techchallenge.domain.gateway.UsuarioGateway;

import java.util.List;
import java.util.Optional;


public class UsuarioUseCase {

    private final UsuarioGateway usuarioGateway;
    private final TipoUsuarioGateway tipoUsuarioGateway;
    private final RestauranteGateway restauranteGateway;

    public UsuarioUseCase(UsuarioGateway usuarioGateway, TipoUsuarioGateway tipoUsuarioGateway,
                          RestauranteGateway restauranteGateway) {
        this.usuarioGateway = usuarioGateway;
        this.tipoUsuarioGateway = tipoUsuarioGateway;
        this.restauranteGateway = restauranteGateway;
    }

    public Usuario criar(UsuarioInput dados) {
        TipoUsuario tipo = buscarTipo(dados.tipoUsuarioId());
        Usuario novoUsuario = new Usuario(null, dados.nome(), dados.email(), dados.login(), tipo);
        verificarEmailRepetido(novoUsuario.getEmail(), null);
        return usuarioGateway.salvar(novoUsuario);
    }

    public Usuario buscarPorId(Long id) {
        return usuarioGateway.buscarPorId(id)
                .orElseThrow(() -> new NaoEncontradoException("Usuário não encontrado com o id " + id));
    }

    public List<Usuario> listarTodos() {
        return usuarioGateway.listarTodos();
    }

    public Usuario atualizar(Long id, UsuarioInput dados) {
        buscarPorId(id); 
        TipoUsuario tipo = buscarTipo(dados.tipoUsuarioId());
        verificarSePodeDeixarDeSerDono(id, tipo);

        Usuario usuarioAtualizado = new Usuario(id, dados.nome(), dados.email(), dados.login(), tipo);
        verificarEmailRepetido(usuarioAtualizado.getEmail(), id);
        return usuarioGateway.salvar(usuarioAtualizado);
    }

    public Usuario alterarTipo(Long id, Long tipoUsuarioId) {
        Usuario usuario = buscarPorId(id);
        TipoUsuario novoTipo = buscarTipo(tipoUsuarioId);
        verificarSePodeDeixarDeSerDono(id, novoTipo);
        return usuarioGateway.salvar(usuario.comTipo(novoTipo));
    }

    public void deletar(Long id) {
        buscarPorId(id);
        if (restauranteGateway.existeRestauranteDoDono(id)) {
            throw new RegraDeNegocioException("Não é possível excluir: o usuário é dono de um ou mais restaurantes");
        }
        usuarioGateway.deletar(id);
    }

    private TipoUsuario buscarTipo(Long tipoUsuarioId) {
        if (tipoUsuarioId == null) {
            throw new ValidacaoException("O campo tipo de usuário é obrigatório");
        }
        return tipoUsuarioGateway.buscarPorId(tipoUsuarioId)
                .orElseThrow(() -> new NaoEncontradoException("Tipo de usuário não encontrado com o id " + tipoUsuarioId));
    }

    
    private void verificarSePodeDeixarDeSerDono(Long usuarioId, TipoUsuario novoTipo) {
        if (!novoTipo.isDonoDeRestaurante() && restauranteGateway.existeRestauranteDoDono(usuarioId)) {
            throw new RegraDeNegocioException(
                    "O usuário é dono de restaurante, então o tipo dele precisa continuar sendo '"
                            + TipoUsuario.DONO_DE_RESTAURANTE + "'");
        }
    }

    private void verificarEmailRepetido(String email, Long idAtual) {
        Optional<Usuario> usuarioComMesmoEmail = usuarioGateway.buscarPorEmail(email);
        if (usuarioComMesmoEmail.isPresent() && !usuarioComMesmoEmail.get().getId().equals(idAtual)) {
            throw new RegraDeNegocioException("Já existe um usuário cadastrado com o email " + email);
        }
    }
}
