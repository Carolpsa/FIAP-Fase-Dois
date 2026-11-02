package br.com.fiap.techchallenge.domain.model;

import br.com.fiap.techchallenge.domain.entities.Restaurante;
import br.com.fiap.techchallenge.domain.entities.TipoUsuario;
import br.com.fiap.techchallenge.domain.exception.RegraDeNegocioException;
import br.com.fiap.techchallenge.domain.exception.ValidacaoException;
import br.com.fiap.techchallenge.util.DadosDeTeste;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RestauranteTest {

    @Test
    void deveCriarRestauranteValido() {
        Restaurante restaurante = new Restaurante(1L, "Cantina", "Rua A, 1", "Italiana",
                "Todos os dias, 11h às 22h", DadosDeTeste.usuarioDono());

        assertThat(restaurante.getId()).isEqualTo(1L);
        assertThat(restaurante.getNome()).isEqualTo("Cantina");
        assertThat(restaurante.getEndereco()).isEqualTo("Rua A, 1");
        assertThat(restaurante.getTipoCozinha()).isEqualTo("Italiana");
        assertThat(restaurante.getHorarioFuncionamento()).isEqualTo("Todos os dias, 11h às 22h");
        assertThat(restaurante.getDono().getNome()).isEqualTo("Maria Souza");
    }

    @Test
    void naoDeveCriarRestauranteSemEndereco() {
        assertThatThrownBy(() -> new Restaurante(1L, "Cantina", " ", "Italiana", "11h às 22h",
                DadosDeTeste.usuarioDono()))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("endereço");
    }

    @Test
    void naoDeveCriarRestauranteSemDono() {
        assertThatThrownBy(() -> new Restaurante(1L, "Cantina", "Rua A", "Italiana", "11h às 22h", null))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("dono");
    }

    @Test
    void naoDeveCriarRestauranteComDonoQueECliente() {
        assertThatThrownBy(() -> new Restaurante(1L, "Cantina", "Rua A", "Italiana", "11h às 22h",
                DadosDeTeste.usuarioCliente()))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining(TipoUsuario.DONO_DE_RESTAURANTE);
    }
}
