package br.com.fiap.techchallenge.domain.entities;


public class Usuario {

    private final Long id;
    private final String nome;
    private final String email;
    private final String login;
    private final TipoUsuario tipoUsuario;

    public Usuario(Long id, String nome, String email, String login, TipoUsuario tipoUsuario) {
        this.id = id;
        this.nome = Validador.textoObrigatorio(nome, "nome", 100);
        this.email = Validador.textoObrigatorio(email, "email", 30);
        this.login = Validador.textoObrigatorio(login, "login", 50);
        this.tipoUsuario = Validador.objetoObrigatorio(tipoUsuario, "tipo de usuário");
    }


    public Usuario comTipo(TipoUsuario novoTipo) {
        return new Usuario(id, nome, email, login, novoTipo);
    }

    public boolean isDonoDeRestaurante() {
        return tipoUsuario.isDonoDeRestaurante();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }
}
