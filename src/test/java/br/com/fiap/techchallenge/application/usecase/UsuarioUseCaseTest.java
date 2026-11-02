package br.com.fiap.techchallenge.application.usecase;

import br.com.fiap.techchallenge.application.dto.UsuarioInput;
import br.com.fiap.techchallenge.domain.entities.Usuario;
import br.com.fiap.techchallenge.domain.exception.NaoEncontradoException;
import br.com.fiap.techchallenge.domain.exception.RegraDeNegocioException;
import br.com.fiap.techchallenge.domain.exception.ValidacaoException;
import br.com.fiap.techchallenge.domain.gateway.RestauranteGateway;
import br.com.fiap.techchallenge.domain.gateway.TipoUsuarioGateway;
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
class UsuarioUseCaseTest {

    @Mock
    private UsuarioGateway usuarioGateway;

    @Mock
    private TipoUsuarioGateway tipoUsuarioGateway;

    @Mock
    private RestauranteGateway restauranteGateway;

    private UsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UsuarioUseCase(usuarioGateway, tipoUsuarioGateway, restauranteGateway);
    }

    private UsuarioInput dadosMaria(Long tipoId) {
        return new UsuarioInput("Maria Souza", "maria@email.com", "maria.souza", tipoId);
    }

    @Test
    void deveCriarUsuario() {
        when(tipoUsuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(DadosDeTeste.tipoDono()));
        when(usuarioGateway.buscarPorEmail("maria@email.com")).thenReturn(Optional.empty());
        when(usuarioGateway.salvar(any(Usuario.class))).thenReturn(DadosDeTeste.usuarioDono());

        Usuario criado = useCase.criar(dadosMaria(1L));

        assertThat(criado.getId()).isEqualTo(10L);
        assertThat(criado.isDonoDeRestaurante()).isTrue();
    }

    @Test
    void naoDeveCriarUsuarioSemTipo() {
        assertThatThrownBy(() -> useCase.criar(dadosMaria(null)))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("tipo de usuário");
    }

    @Test
    void naoDeveCriarUsuarioComTipoInexistente() {
        when(tipoUsuarioGateway.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.criar(dadosMaria(99L)))
                .isInstanceOf(NaoEncontradoException.class);
    }

    @Test
    void naoDeveCriarUsuarioComEmailRepetido() {
        when(tipoUsuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(DadosDeTeste.tipoDono()));
        when(usuarioGateway.buscarPorEmail("maria@email.com")).thenReturn(Optional.of(DadosDeTeste.usuarioDono()));

        assertThatThrownBy(() -> useCase.criar(dadosMaria(1L)))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("email");
        verify(usuarioGateway, never()).salvar(any());
    }

    @Test
    void deveBuscarUsuarioPorId() {
        when(usuarioGateway.buscarPorId(10L)).thenReturn(Optional.of(DadosDeTeste.usuarioDono()));

        assertThat(useCase.buscarPorId(10L).getNome()).isEqualTo("Maria Souza");
    }

    @Test
    void deveLancarErroQuandoUsuarioNaoExiste() {
        when(usuarioGateway.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.buscarPorId(99L))
                .isInstanceOf(NaoEncontradoException.class);
    }

    @Test
    void deveListarUsuarios() {
        when(usuarioGateway.listarTodos()).thenReturn(List.of(DadosDeTeste.usuarioDono(), DadosDeTeste.usuarioCliente()));

        assertThat(useCase.listarTodos()).hasSize(2);
    }

    @Test
    void deveAtualizarUsuario() {
        UsuarioInput novosDados = new UsuarioInput("Maria S.", "maria@email.com", "maria", 1L);
        when(usuarioGateway.buscarPorId(10L)).thenReturn(Optional.of(DadosDeTeste.usuarioDono()));
        when(tipoUsuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(DadosDeTeste.tipoDono()));
        when(usuarioGateway.buscarPorEmail("maria@email.com")).thenReturn(Optional.of(DadosDeTeste.usuarioDono()));
        when(usuarioGateway.salvar(any(Usuario.class)))
                .thenReturn(new Usuario(10L, "Maria S.", "maria@email.com", "maria", DadosDeTeste.tipoDono()));

        Usuario atualizado = useCase.atualizar(10L, novosDados);

        assertThat(atualizado.getNome()).isEqualTo("Maria S.");
    }

    @Test
    void naoDeveTransformarDonoComRestauranteEmCliente() {
        when(usuarioGateway.buscarPorId(10L)).thenReturn(Optional.of(DadosDeTeste.usuarioDono()));
        when(tipoUsuarioGateway.buscarPorId(2L)).thenReturn(Optional.of(DadosDeTeste.tipoCliente()));
        when(restauranteGateway.existeRestauranteDoDono(10L)).thenReturn(true);

        assertThatThrownBy(() -> useCase.atualizar(10L, dadosMaria(2L)))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("dono de restaurante");
    }

    @Test
    void deveAlterarTipoDoUsuario() {
        when(usuarioGateway.buscarPorId(20L)).thenReturn(Optional.of(DadosDeTeste.usuarioCliente()));
        when(tipoUsuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(DadosDeTeste.tipoDono()));
        when(usuarioGateway.salvar(any(Usuario.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        Usuario atualizado = useCase.alterarTipo(20L, 1L);

        assertThat(atualizado.isDonoDeRestaurante()).isTrue();
        assertThat(atualizado.getNome()).isEqualTo("João Silva");
    }

    @Test
    void devePermitirDonoSemRestauranteVirarCliente() {
        when(usuarioGateway.buscarPorId(10L)).thenReturn(Optional.of(DadosDeTeste.usuarioDono()));
        when(tipoUsuarioGateway.buscarPorId(2L)).thenReturn(Optional.of(DadosDeTeste.tipoCliente()));
        when(restauranteGateway.existeRestauranteDoDono(10L)).thenReturn(false);
        when(usuarioGateway.salvar(any(Usuario.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        Usuario atualizado = useCase.alterarTipo(10L, 2L);

        assertThat(atualizado.isDonoDeRestaurante()).isFalse();
    }

    @Test
    void deveDeletarUsuarioSemRestaurante() {
        when(usuarioGateway.buscarPorId(20L)).thenReturn(Optional.of(DadosDeTeste.usuarioCliente()));
        when(restauranteGateway.existeRestauranteDoDono(20L)).thenReturn(false);

        useCase.deletar(20L);

        verify(usuarioGateway).deletar(20L);
    }

    @Test
    void naoDeveDeletarUsuarioQueEhDonoDeRestaurante() {
        when(usuarioGateway.buscarPorId(10L)).thenReturn(Optional.of(DadosDeTeste.usuarioDono()));
        when(restauranteGateway.existeRestauranteDoDono(10L)).thenReturn(true);

        assertThatThrownBy(() -> useCase.deletar(10L))
                .isInstanceOf(RegraDeNegocioException.class);
        verify(usuarioGateway, never()).deletar(any());
    }
}
