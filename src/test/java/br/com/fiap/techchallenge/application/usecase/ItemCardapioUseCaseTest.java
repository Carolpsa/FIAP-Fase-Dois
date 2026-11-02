package br.com.fiap.techchallenge.application.usecase;

import br.com.fiap.techchallenge.application.dto.ItemCardapioInput;
import br.com.fiap.techchallenge.domain.entities.ItemCardapio;
import br.com.fiap.techchallenge.domain.exception.NaoEncontradoException;
import br.com.fiap.techchallenge.domain.exception.ValidacaoException;
import br.com.fiap.techchallenge.domain.gateway.ItemCardapioGateway;
import br.com.fiap.techchallenge.domain.gateway.RestauranteGateway;
import br.com.fiap.techchallenge.util.DadosDeTeste;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemCardapioUseCaseTest {

    @Mock
    private ItemCardapioGateway itemCardapioGateway;

    @Mock
    private RestauranteGateway restauranteGateway;

    private ItemCardapioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ItemCardapioUseCase(itemCardapioGateway, restauranteGateway);
    }

    private ItemCardapioInput dadosLasanha(Long restauranteId, BigDecimal preco) {
        return new ItemCardapioInput("Lasanha", "Lasanha à bolonhesa", preco, false,
                "/imagens/lasanha.jpg", restauranteId);
    }

    @Test
    void deveCriarItem() {
        when(restauranteGateway.buscarPorId(100L)).thenReturn(Optional.of(DadosDeTeste.restaurante()));
        when(itemCardapioGateway.salvar(any(ItemCardapio.class))).thenReturn(DadosDeTeste.itemCardapio());

        ItemCardapio criado = useCase.criar(dadosLasanha(100L, new BigDecimal("49.90")));

        assertThat(criado.getId()).isEqualTo(1000L);
        assertThat(criado.getRestaurante().getId()).isEqualTo(100L);
    }

    @Test
    void naoDeveCriarItemComPrecoInvalido() {
        when(restauranteGateway.buscarPorId(100L)).thenReturn(Optional.of(DadosDeTeste.restaurante()));

        assertThatThrownBy(() -> useCase.criar(dadosLasanha(100L, new BigDecimal("-1"))))
                .isInstanceOf(ValidacaoException.class);
        verify(itemCardapioGateway, never()).salvar(any());
    }

    @Test
    void naoDeveCriarItemSemRestaurante() {
        assertThatThrownBy(() -> useCase.criar(dadosLasanha(null, BigDecimal.TEN)))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("restaurante");
    }

    @Test
    void naoDeveCriarItemEmRestauranteInexistente() {
        when(restauranteGateway.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.criar(dadosLasanha(99L, BigDecimal.TEN)))
                .isInstanceOf(NaoEncontradoException.class);
    }

    @Test
    void deveBuscarItemPorId() {
        when(itemCardapioGateway.buscarPorId(1000L)).thenReturn(Optional.of(DadosDeTeste.itemCardapio()));

        assertThat(useCase.buscarPorId(1000L).getNome()).isEqualTo("Lasanha");
    }

    @Test
    void deveLancarErroQuandoItemNaoExiste() {
        when(itemCardapioGateway.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.buscarPorId(99L))
                .isInstanceOf(NaoEncontradoException.class);
    }

    @Test
    void deveListarTodosOsItens() {
        when(itemCardapioGateway.listarTodos()).thenReturn(List.of(DadosDeTeste.itemCardapio()));

        assertThat(useCase.listarTodos()).hasSize(1);
    }

    @Test
    void deveListarItensDoRestaurante() {
        when(restauranteGateway.buscarPorId(100L)).thenReturn(Optional.of(DadosDeTeste.restaurante()));
        when(itemCardapioGateway.listarPorRestaurante(100L)).thenReturn(List.of(DadosDeTeste.itemCardapio()));

        assertThat(useCase.listarPorRestaurante(100L)).hasSize(1);
    }

    @Test
    void deveAtualizarItem() {
        when(itemCardapioGateway.buscarPorId(1000L)).thenReturn(Optional.of(DadosDeTeste.itemCardapio()));
        when(restauranteGateway.buscarPorId(100L)).thenReturn(Optional.of(DadosDeTeste.restaurante()));
        when(itemCardapioGateway.salvar(any(ItemCardapio.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        ItemCardapio atualizado = useCase.atualizar(1000L, dadosLasanha(100L, new BigDecimal("55.00")));

        assertThat(atualizado.getId()).isEqualTo(1000L);
        assertThat(atualizado.getPreco()).isEqualByComparingTo("55.00");
    }

    @Test
    void deveDeletarItem() {
        when(itemCardapioGateway.buscarPorId(1000L)).thenReturn(Optional.of(DadosDeTeste.itemCardapio()));

        useCase.deletar(1000L);

        verify(itemCardapioGateway).deletar(1000L);
    }
}
