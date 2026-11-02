package br.com.fiap.techchallenge.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "restaurantes")
public class RestauranteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false)
    private String endereco;

    @Column(name = "tipo_cozinha", nullable = false, length = 50)
    private String tipoCozinha;

    @Column(name = "horario_funcionamento", nullable = false, length = 100)
    private String horarioFuncionamento;

    @ManyToOne
    @JoinColumn(name = "dono_id", nullable = false)
    private UsuarioEntity dono;

    public RestauranteEntity() {
    }

    public RestauranteEntity(Long id, String nome, String endereco, String tipoCozinha,
                             String horarioFuncionamento, UsuarioEntity dono) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.tipoCozinha = tipoCozinha;
        this.horarioFuncionamento = horarioFuncionamento;
        this.dono = dono;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTipoCozinha() {
        return tipoCozinha;
    }

    public void setTipoCozinha(String tipoCozinha) {
        this.tipoCozinha = tipoCozinha;
    }

    public String getHorarioFuncionamento() {
        return horarioFuncionamento;
    }

    public void setHorarioFuncionamento(String horarioFuncionamento) {
        this.horarioFuncionamento = horarioFuncionamento;
    }

    public UsuarioEntity getDono() {
        return dono;
    }

    public void setDono(UsuarioEntity dono) {
        this.dono = dono;
    }
}
