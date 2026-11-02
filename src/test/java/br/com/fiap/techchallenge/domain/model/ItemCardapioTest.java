package br.com.fiap.techchallenge.domain.model;

import br.com.fiap.techchallenge.domain.entities.ItemCardapio;
import br.com.fiap.techchallenge.domain.exception.ValidacaoException;
import br.com.fiap.techchallenge.util.DadosDeTeste;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ItemCardapioTest {

    @Test
    void deveCriarItemValido() {
        ItemCardapio item = new ItemCardapio(1L, "Pizza", "Pizza de calabresa", new BigDecimal("59.90"),
                true, "/fotos/pizza.jpg", DadosDeTeste.restaurante());

        assertThat(item.getId()).isEqualTo(1L);
        assertThat(item.getNome()).isEqualTo("Pizza");
        assertThat(item.getDescricao()).isEqualTo("Pizza de calabresa");
        assertThat(item.getPreco()).isEqualByComparingTo("59.90");
        assertThat(item.isDisponivelApenasNoLocal()).isTrue();
        assertThat(item.getCaminhoFoto()).isEqualTo("/fotos/pizza.jpg");
        assertThat(item.getRestaurante().getNome()).isEqualTo("Cantina da Nona");
    }

    @Test
    void naoDeveCriarItemSemPreco() {
        assertThatThrownBy(() -> new ItemCardapio(1L, "Pizza", "Desc", null, false, null, DadosDeTeste.restaurante()))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("preço");
    }

    @Test
    void naoDeveCriarItemComPrecoZeroOuNegativo() {
        assertThatThrownBy(() -> new ItemCardapio(1L, "Pizza", "Desc", BigDecimal.ZERO, false, null, DadosDeTeste.restaurante()))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("maior que zero");

        assertThatThrownBy(() -> new ItemCardapio(1L, "Pizza", "Desc", new BigDecimal("-5"), false, null, DadosDeTeste.restaurante()))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("maior que zero");
    }

    @Test
    void naoDeveCriarItemSemDescricao() {
        assertThatThrownBy(() -> new ItemCardapio(1L, "Pizza", null, BigDecimal.TEN, false, null, DadosDeTeste.restaurante()))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("descrição");
    }

    @Test
    void naoDeveCriarItemSemRestaurante() {
        assertThatThrownBy(() -> new ItemCardapio(1L, "Pizza", "Desc", BigDecimal.TEN, false, null, null))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("restaurante");
    }
}
