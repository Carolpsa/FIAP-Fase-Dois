package br.com.fiap.techchallenge.util;

import java.math.BigDecimal;

import br.com.fiap.techchallenge.domain.entities.ItemCardapio;
import br.com.fiap.techchallenge.domain.entities.Restaurante;
import br.com.fiap.techchallenge.domain.entities.TipoUsuario;
import br.com.fiap.techchallenge.domain.entities.Usuario;


public class DadosDeTeste {

    public static TipoUsuario tipoDono() {
        return new TipoUsuario(1L, TipoUsuario.DONO_DE_RESTAURANTE);
    }

    public static TipoUsuario tipoCliente() {
        return new TipoUsuario(2L, TipoUsuario.CLIENTE);
    }

    public static TipoUsuario tipoPersonalizado() {
        return new TipoUsuario(3L, "Funcionário");
    }

    public static Usuario usuarioDono() {
        return new Usuario(10L, "Maria Souza", "maria@email.com", "maria.souza", tipoDono());
    }

    public static Usuario usuarioCliente() {
        return new Usuario(20L, "João Silva", "joao@email.com", "joao.silva", tipoCliente());
    }

    public static Restaurante restaurante() {
        return new Restaurante(100L, "Cantina da Nona", "Rua das Flores, 123", "Italiana",
                "Terça a Domingo, das 11h às 23h", usuarioDono());
    }

    public static ItemCardapio itemCardapio() {
        return new ItemCardapio(1000L, "Lasanha", "Lasanha à bolonhesa", new BigDecimal("49.90"),
                false, "/imagens/lasanha.jpg", restaurante());
    }
}
