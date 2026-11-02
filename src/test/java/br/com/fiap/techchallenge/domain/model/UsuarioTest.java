package br.com.fiap.techchallenge.domain.model;

import br.com.fiap.techchallenge.domain.entities.TipoUsuario;
import br.com.fiap.techchallenge.domain.entities.Usuario;
import br.com.fiap.techchallenge.domain.exception.ValidacaoException;
import br.com.fiap.techchallenge.util.DadosDeTeste;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UsuarioTest {

    @Test
    void deveCriarUsuarioValido() {
        Usuario usuario = new Usuario(1L, "Maria", "maria@email.com", "maria", DadosDeTeste.tipoDono());

        assertThat(usuario.getId()).isEqualTo(1L);
        assertThat(usuario.getNome()).isEqualTo("Maria");
        assertThat(usuario.getEmail()).isEqualTo("maria@email.com"); // email fica minúsculo
        assertThat(usuario.getLogin()).isEqualTo("maria");
        assertThat(usuario.getTipoUsuario().getNome()).isEqualTo(TipoUsuario.DONO_DE_RESTAURANTE);
        assertThat(usuario.isDonoDeRestaurante()).isTrue();
    }

    @Test
    void naoDeveCriarUsuarioSemNome() {
        assertThatThrownBy(() -> new Usuario(1L, "", "maria@email.com", "maria", DadosDeTeste.tipoDono()))
                .isInstanceOf(ValidacaoException.class);
    }

    @Test
    void naoDeveCriarUsuarioSemEmail() {
        assertThatThrownBy(() -> new Usuario(1L, "Maria", "", "maria", DadosDeTeste.tipoDono()))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("email");
    }

    @Test
    void naoDeveCriarUsuarioSemLogin() {
        assertThatThrownBy(() -> new Usuario(1L, "Maria", "maria@email.com", null, DadosDeTeste.tipoDono()))
                .isInstanceOf(ValidacaoException.class);
    }

    @Test
    void naoDeveCriarUsuarioSemTipo() {
        assertThatThrownBy(() -> new Usuario(1L, "Maria", "maria@email.com", "maria", null))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("tipo de usuário");
    }

    @Test
    void deveCriarCopiaComOutroTipo() {
        Usuario cliente = DadosDeTeste.usuarioCliente();

        Usuario virouDono = cliente.comTipo(DadosDeTeste.tipoDono());

        assertThat(virouDono.getId()).isEqualTo(cliente.getId());
        assertThat(virouDono.isDonoDeRestaurante()).isTrue();
        assertThat(cliente.isDonoDeRestaurante()).isFalse();
    }
}
