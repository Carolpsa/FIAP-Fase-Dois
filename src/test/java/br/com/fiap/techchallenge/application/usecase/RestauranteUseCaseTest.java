package br.com.fiap.techchallenge.application.usecase;

import br.com.fiap.techchallenge.application.dto.RestauranteInput;
import br.com.fiap.techchallenge.domain.entities.Restaurante;
import br.com.fiap.techchallenge.domain.exception.NaoEncontradoException;
import br.com.fiap.techchallenge.domain.exception.RegraDeNegocioException;
import br.com.fiap.techchallenge.domain.exception.ValidacaoException;
import br.com.fiap.techchallenge.domain.gateway.ItemCardapioGateway;
import br.com.fiap.techchallenge.domain.gateway.RestauranteGateway;
import br.com.fiap.techchallenge.domain.gateway.UsuarioGateway;
import br.com.fiap.techchallenge.util.DadosDeTeste;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestauranteUseCaseTest {

    @Mock
    private RestauranteGateway restauranteGateway;

    @Mock
    private UsuarioGateway usuarioGateway;

    @Mock
    private ItemCardapioGateway itemCardapioGateway;

    private RestauranteUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new RestauranteUseCase(restauranteGateway, usuarioGateway, itemCardapioGateway);
    }

    private RestauranteInput dadosCantina(Long donoId) {
        return new RestauranteInput("Cantina da Nona", "Rua das Flores, 123", "Italiana",
                "Terça a Domingo, das 11h às 23h", donoId);
    }

    @Test
    void deveCriarRestaurante() {
        when(usuarioGateway.buscarPorId(10L)).thenReturn(Optional.of(DadosDeTeste.usuarioDono()));
        when(restauranteGateway.salvar(any(Restaurante.class))).thenReturn(DadosDeTeste.restaurante());

        Restaurante criado = useCase.criar(dadosCantina(10L));

        assertThat(criado.getId()).isEqualTo(100L);
        assertThat(criado.getDono().getId()).isEqualTo(10L);
    }

    @Test
    void naoDeveCriarRestauranteComDonoCliente() {
        when(usuarioGateway.buscarPorId(20L)).thenReturn(Optional.of(DadosDeTeste.usuarioCliente()));

        assertThatThrownBy(() -> useCase.criar(dadosCantina(20L)))
                .isInstanceOf(RegraDeNegocioException.class);
        verify(restauranteGateway, never()).salvar(any());
    }

    @Test
    void naoDeveCriarRestauranteSemDono() {
        assertThatThrownBy(() -> useCase.criar(dadosCantina(null)))
                .isInstanceOf(ValidacaoException.class);
    }

    @Test
    void naoDeveCriarRestauranteComDonoInexistente() {
        when(usuarioGateway.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.criar(dadosCantina(99L)))
                .isInstanceOf(NaoEncontradoException.class)
                .hasMessageContaining("dono");
    }

    @Test
    void deveBuscarRestaurantePorId() {
        when(restauranteGateway.buscarPorId(100L)).thenReturn(Optional.of(DadosDeTeste.restaurante()));

        assertThat(useCase.buscarPorId(100L).getNome()).isEqualTo("Cantina da Nona");
    }

    @Test
    void deveLancarErroQuandoRestauranteNaoExiste() {
        when(restauranteGateway.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.buscarPorId(99L))
                .isInstanceOf(NaoEncontradoException.class);
    }

    @Test
    void deveListarRestaurantes() {
        when(restauranteGateway.listarTodos()).thenReturn(List.of(DadosDeTeste.restaurante()));

        assertThat(useCase.listarTodos()).hasSize(1);
    }

    @Test
    void deveAtualizarRestaurante() {
        when(restauranteGateway.buscarPorId(100L)).thenReturn(Optional.of(DadosDeTeste.restaurante()));
        when(usuarioGateway.buscarPorId(10L)).thenReturn(Optional.of(DadosDeTeste.usuarioDono()));
        when(restauranteGateway.salvar(any(Restaurante.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        RestauranteInput novosDados = new RestauranteInput("Cantina Nova", "Rua B, 2", "Italiana",
                "Todos os dias", 10L);
        Restaurante atualizado = useCase.atualizar(100L, novosDados);

        assertThat(atualizado.getId()).isEqualTo(100L);
        assertThat(atualizado.getNome()).isEqualTo("Cantina Nova");
    }

    @Test
    void deveDeletarRestauranteSemItens() {
        when(restauranteGateway.buscarPorId(100L)).thenReturn(Optional.of(DadosDeTeste.restaurante()));
        when(itemCardapioGateway.existeItemNoRestaurante(100L)).thenReturn(false);

        useCase.deletar(100L);

        verify(restauranteGateway).deletar(100L);
    }

    @Test
    void naoDeveDeletarRestauranteComItens() {
        when(restauranteGateway.buscarPorId(100L)).thenReturn(Optional.of(DadosDeTeste.restaurante()));
        when(itemCardapioGateway.existeItemNoRestaurante(100L)).thenReturn(true);

        assertThatThrownBy(() -> useCase.deletar(100L))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("itens");
        verify(restauranteGateway, never()).deletar(any());
    }
}
