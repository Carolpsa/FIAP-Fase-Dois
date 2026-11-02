package br.com.fiap.techchallenge.application.usecase;

import br.com.fiap.techchallenge.application.dto.RestauranteInput;
import br.com.fiap.techchallenge.domain.entities.Restaurante;
import br.com.fiap.techchallenge.domain.entities.Usuario;
import br.com.fiap.techchallenge.domain.exception.NaoEncontradoException;
import br.com.fiap.techchallenge.domain.exception.RegraDeNegocioException;
import br.com.fiap.techchallenge.domain.exception.ValidacaoException;
import br.com.fiap.techchallenge.domain.gateway.ItemCardapioGateway;
import br.com.fiap.techchallenge.domain.gateway.RestauranteGateway;
import br.com.fiap.techchallenge.domain.gateway.UsuarioGateway;

import java.util.List;


public class RestauranteUseCase {

    private final RestauranteGateway restauranteGateway;
    private final UsuarioGateway usuarioGateway;
    private final ItemCardapioGateway itemCardapioGateway;

    public RestauranteUseCase(RestauranteGateway restauranteGateway, UsuarioGateway usuarioGateway,
                              ItemCardapioGateway itemCardapioGateway) {
        this.restauranteGateway = restauranteGateway;
        this.usuarioGateway = usuarioGateway;
        this.itemCardapioGateway = itemCardapioGateway;
    }

    public Restaurante criar(RestauranteInput dados) {
        Usuario dono = buscarDono(dados.donoId());
        Restaurante novoRestaurante = new Restaurante(null, dados.nome(), dados.endereco(),
                dados.tipoCozinha(), dados.horarioFuncionamento(), dono);
        return restauranteGateway.salvar(novoRestaurante);
    }

    public Restaurante buscarPorId(Long id) {
        return restauranteGateway.buscarPorId(id)
                .orElseThrow(() -> new NaoEncontradoException("Restaurante não encontrado com o id " + id));
    }

    public List<Restaurante> listarTodos() {
        return restauranteGateway.listarTodos();
    }

    public Restaurante atualizar(Long id, RestauranteInput dados) {
        buscarPorId(id);
        Usuario dono = buscarDono(dados.donoId());
        Restaurante restauranteAtualizado = new Restaurante(id, dados.nome(), dados.endereco(),
                dados.tipoCozinha(), dados.horarioFuncionamento(), dono);
        return restauranteGateway.salvar(restauranteAtualizado);
    }

    public void deletar(Long id) {
        buscarPorId(id);
        if (itemCardapioGateway.existeItemNoRestaurante(id)) {
            throw new RegraDeNegocioException("Não é possível excluir: o restaurante ainda tem itens no cardápio");
        }
        restauranteGateway.deletar(id);
    }

    private Usuario buscarDono(Long donoId) {
        if (donoId == null) {
            throw new ValidacaoException("O campo dono do restaurante é obrigatório");
        }
        return usuarioGateway.buscarPorId(donoId)
                .orElseThrow(() -> new NaoEncontradoException("Usuário dono não encontrado com o id " + donoId));
    }
}
