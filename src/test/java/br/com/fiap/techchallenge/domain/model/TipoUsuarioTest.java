package br.com.fiap.techchallenge.domain.model;

import br.com.fiap.techchallenge.domain.entities.TipoUsuario;
import br.com.fiap.techchallenge.domain.exception.ValidacaoException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TipoUsuarioTest {

    @Test
    void deveCriarTipoUsuarioValido() {
        TipoUsuario tipo = new TipoUsuario(1L, "  Funcionário  ");

        assertThat(tipo.getId()).isEqualTo(1L);
        assertThat(tipo.getNome()).isEqualTo("Funcionário");
    }

    @Test
    void naoDeveCriarTipoSemNome() {
        assertThatThrownBy(() -> new TipoUsuario(1L, null))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("nome");

        assertThatThrownBy(() -> new TipoUsuario(1L, "   "))
                .isInstanceOf(ValidacaoException.class);
    }

    @Test
    void naoDeveCriarTipoComNomeMuitoGrande() {
        String nomeGrande = "a".repeat(51);

        assertThatThrownBy(() -> new TipoUsuario(1L, nomeGrande))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("50");
    }

    @Test
    void deveIdentificarTipoDonoDeRestauranteSemImportarMaiusculas() {
        assertThat(new TipoUsuario(1L, "dono de restaurante").isDonoDeRestaurante()).isTrue();
        assertThat(new TipoUsuario(2L, "Cliente").isDonoDeRestaurante()).isFalse();
    }

    @Test
    void deveIdentificarTiposPadrao() {
        assertThat(new TipoUsuario(1L, "Dono de Restaurante").isTipoPadrao()).isTrue();
        assertThat(new TipoUsuario(2L, "CLIENTE").isTipoPadrao()).isTrue();
        assertThat(new TipoUsuario(3L, "Funcionário").isTipoPadrao()).isFalse();
    }
}
