package br.com.fiap.techchallenge.domain.entities;

import br.com.fiap.techchallenge.domain.exception.RegraDeNegocioException;


public class Restaurante {

    private final Long id;
    private final String nome;
    private final String endereco;
    private final String tipoCozinha;
    private final String horarioFuncionamento;
    private final Usuario dono;

    public Restaurante(Long id, String nome, String endereco, String tipoCozinha,
                       String horarioFuncionamento, Usuario dono) {
        this.id = id;
        this.nome = Validador.textoObrigatorio(nome, "nome", 100);
        this.endereco = Validador.textoObrigatorio(endereco, "endereço", 255);
        this.tipoCozinha = Validador.textoObrigatorio(tipoCozinha, "tipo de cozinha", 50);
        this.horarioFuncionamento = Validador.textoObrigatorio(horarioFuncionamento, "horário de funcionamento", 100);
        this.dono = validarDono(dono);
    }

    private Usuario validarDono(Usuario dono) {
        Validador.objetoObrigatorio(dono, "dono do restaurante");
        if (!dono.isDonoDeRestaurante()) {
            throw new RegraDeNegocioException(
                    "O usuário " + dono.getNome() + " não é do tipo '" + TipoUsuario.DONO_DE_RESTAURANTE + "'");
        }
        return dono;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getTipoCozinha() {
        return tipoCozinha;
    }

    public String getHorarioFuncionamento() {
        return horarioFuncionamento;
    }

    public Usuario getDono() {
        return dono;
    }
}
