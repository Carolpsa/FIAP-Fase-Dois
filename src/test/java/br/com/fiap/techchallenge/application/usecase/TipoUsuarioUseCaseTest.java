package br.com.fiap.techchallenge.application.usecase;

import br.com.fiap.techchallenge.domain.entities.TipoUsuario;
import br.com.fiap.techchallenge.domain.exception.NaoEncontradoException;
import br.com.fiap.techchallenge.domain.exception.RegraDeNegocioException;
import br.com.fiap.techchallenge.domain.exception.ValidacaoException;
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
class TipoUsuarioUseCaseTest {

    @Mock
    private TipoUsuarioGateway tipoUsuarioGateway;

    @Mock
    private UsuarioGateway usuarioGateway;

    private TipoUsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new TipoUsuarioUseCase(tipoUsuarioGateway, usuarioGateway);
    }

    @Test
    void deveCriarTipoUsuario() {
        when(tipoUsuarioGateway.buscarPorNome("Funcionário")).thenReturn(Optional.empty());
        when(tipoUsuarioGateway.salvar(any(TipoUsuario.class))).thenReturn(DadosDeTeste.tipoPersonalizado());

        TipoUsuario criado = useCase.criar("Funcionário");

        assertThat(criado.getId()).isEqualTo(3L);
        assertThat(criado.getNome()).isEqualTo("Funcionário");
    }

    @Test
    void naoDeveCriarTipoComNomeRepetido() {
        when(tipoUsuarioGateway.buscarPorNome("Funcionário")).thenReturn(Optional.of(DadosDeTeste.tipoPersonalizado()));

        assertThatThrownBy(() -> useCase.criar("Funcionário"))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("Já existe");
        verify(tipoUsuarioGateway, never()).salvar(any());
    }

    @Test
    void naoDeveCriarTipoComNomeVazio() {
        assertThatThrownBy(() -> useCase.criar(""))
                .isInstanceOf(ValidacaoException.class);
    }

    @Test
    void deveBuscarTipoPorId() {
        when(tipoUsuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(DadosDeTeste.tipoDono()));

        TipoUsuario encontrado = useCase.buscarPorId(1L);

        assertThat(encontrado.getNome()).isEqualTo(TipoUsuario.DONO_DE_RESTAURANTE);
    }

    @Test
    void deveLancarErroQuandoTipoNaoExiste() {
        when(tipoUsuarioGateway.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.buscarPorId(99L))
                .isInstanceOf(NaoEncontradoException.class)
                .hasMessageContaining("99");
    }

    @Test
    void deveListarTodosOsTipos() {
        when(tipoUsuarioGateway.listarTodos()).thenReturn(List.of(DadosDeTeste.tipoDono(), DadosDeTeste.tipoCliente()));

        assertThat(useCase.listarTodos()).hasSize(2);
    }

    @Test
    void deveAtualizarTipo() {
        when(tipoUsuarioGateway.buscarPorId(3L)).thenReturn(Optional.of(DadosDeTeste.tipoPersonalizado()));
        when(tipoUsuarioGateway.buscarPorNome("Garçom")).thenReturn(Optional.empty());
        when(tipoUsuarioGateway.salvar(any(TipoUsuario.class))).thenReturn(new TipoUsuario(3L, "Garçom"));

        TipoUsuario atualizado = useCase.atualizar(3L, "Garçom");

        assertThat(atualizado.getNome()).isEqualTo("Garçom");
    }

    @Test
    void devePermitirAtualizarMantendoOMesmoNome() {
        when(tipoUsuarioGateway.buscarPorId(3L)).thenReturn(Optional.of(DadosDeTeste.tipoPersonalizado()));
        when(tipoUsuarioGateway.buscarPorNome("Funcionário")).thenReturn(Optional.of(DadosDeTeste.tipoPersonalizado()));
        when(tipoUsuarioGateway.salvar(any(TipoUsuario.class))).thenReturn(DadosDeTeste.tipoPersonalizado());

        TipoUsuario atualizado = useCase.atualizar(3L, "Funcionário");

        assertThat(atualizado.getNome()).isEqualTo("Funcionário");
    }

    @Test
    void naoDeveAtualizarTipoPadrao() {
        when(tipoUsuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(DadosDeTeste.tipoDono()));

        assertThatThrownBy(() -> useCase.atualizar(1L, "Outro nome"))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("padrão");
    }

    @Test
    void deveDeletarTipoSemUsuarios() {
        when(tipoUsuarioGateway.buscarPorId(3L)).thenReturn(Optional.of(DadosDeTeste.tipoPersonalizado()));
        when(usuarioGateway.existeUsuarioComTipo(3L)).thenReturn(false);

        useCase.deletar(3L);

        verify(tipoUsuarioGateway).deletar(3L);
    }

    @Test
    void naoDeveDeletarTipoComUsuarios() {
        when(tipoUsuarioGateway.buscarPorId(3L)).thenReturn(Optional.of(DadosDeTeste.tipoPersonalizado()));
        when(usuarioGateway.existeUsuarioComTipo(3L)).thenReturn(true);

        assertThatThrownBy(() -> useCase.deletar(3L))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("usuários associados");
        verify(tipoUsuarioGateway, never()).deletar(any());
    }

    @Test
    void naoDeveDeletarTipoPadrao() {
        when(tipoUsuarioGateway.buscarPorId(2L)).thenReturn(Optional.of(DadosDeTeste.tipoCliente()));

        assertThatThrownBy(() -> useCase.deletar(2L))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("padrão");
    }
}
