package br.com.fiap.techchallenge.application.usecase;

import br.com.fiap.techchallenge.domain.entities.TipoUsuario;
import br.com.fiap.techchallenge.domain.exception.NaoEncontradoException;
import br.com.fiap.techchallenge.domain.exception.RegraDeNegocioException;
import br.com.fiap.techchallenge.domain.gateway.TipoUsuarioGateway;
import br.com.fiap.techchallenge.domain.gateway.UsuarioGateway;

import java.util.List;
import java.util.Optional;


public class TipoUsuarioUseCase {

    private final TipoUsuarioGateway tipoUsuarioGateway;
    private final UsuarioGateway usuarioGateway;

    public TipoUsuarioUseCase(TipoUsuarioGateway tipoUsuarioGateway, UsuarioGateway usuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
        this.usuarioGateway = usuarioGateway;
    }

    public TipoUsuario criar(String nome) {
        TipoUsuario novoTipo = new TipoUsuario(null, nome);
        verificarNomeRepetido(novoTipo.getNome(), null);
        return tipoUsuarioGateway.salvar(novoTipo);
    }

    public TipoUsuario buscarPorId(Long id) {
        return tipoUsuarioGateway.buscarPorId(id)
                .orElseThrow(() -> new NaoEncontradoException("Tipo de usuário não encontrado com o id " + id));
    }

    public List<TipoUsuario> listarTodos() {
        return tipoUsuarioGateway.listarTodos();
    }

    public TipoUsuario atualizar(Long id, String nome) {
        TipoUsuario tipoAtual = buscarPorId(id);
        if (tipoAtual.isTipoPadrao()) {
            throw new RegraDeNegocioException("O tipo '" + tipoAtual.getNome() + "' é padrão do sistema e não pode ser alterado");
        }

        TipoUsuario tipoAtualizado = new TipoUsuario(id, nome);
        verificarNomeRepetido(tipoAtualizado.getNome(), id);
        return tipoUsuarioGateway.salvar(tipoAtualizado);
    }

    public void deletar(Long id) {
        TipoUsuario tipoAtual = buscarPorId(id);
        if (tipoAtual.isTipoPadrao()) {
            throw new RegraDeNegocioException("O tipo '" + tipoAtual.getNome() + "' é padrão do sistema e não pode ser excluído");
        }
        if (usuarioGateway.existeUsuarioComTipo(id)) {
            throw new RegraDeNegocioException("Não é possível excluir: existem usuários associados a esse tipo");
        }
        tipoUsuarioGateway.deletar(id);
    }

    
    private void verificarNomeRepetido(String nome, Long idAtual) {
        Optional<TipoUsuario> tipoComMesmoNome = tipoUsuarioGateway.buscarPorNome(nome);
        if (tipoComMesmoNome.isPresent() && !tipoComMesmoNome.get().getId().equals(idAtual)) {
            throw new RegraDeNegocioException("Já existe um tipo de usuário com o nome '" + nome + "'");
        }
    }
}
