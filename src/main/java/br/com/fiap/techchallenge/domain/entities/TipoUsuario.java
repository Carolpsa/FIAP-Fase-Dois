package br.com.fiap.techchallenge.domain.entities;


public class TipoUsuario {

    public static final String DONO_DE_RESTAURANTE = "Dono de Restaurante";
    public static final String CLIENTE = "Cliente";

    private final Long id;
    private final String nome;

    public TipoUsuario(Long id, String nome) {
        this.id = id;
        this.nome = Validador.textoObrigatorio(nome, "nome", 50);
    }

    public boolean isDonoDeRestaurante() {
        return DONO_DE_RESTAURANTE.equalsIgnoreCase(nome);
    }

    public boolean isTipoPadrao() {
        return DONO_DE_RESTAURANTE.equalsIgnoreCase(nome) || CLIENTE.equalsIgnoreCase(nome);
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}
