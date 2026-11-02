package br.com.fiap.techchallenge.domain.entities;

import br.com.fiap.techchallenge.domain.exception.ValidacaoException;

import java.math.BigDecimal;


public class ItemCardapio {

    private final Long id;
    private final String nome;
    private final String descricao;
    private final BigDecimal preco;
    private final boolean disponivelApenasNoLocal;
    private final String caminhoFoto;
    private final Restaurante restaurante;

    public ItemCardapio(Long id, String nome, String descricao, BigDecimal preco,
                        boolean disponivelApenasNoLocal, String caminhoFoto, Restaurante restaurante) {
        this.id = id;
        this.nome = Validador.textoObrigatorio(nome, "nome", 100);
        this.descricao = Validador.textoObrigatorio(descricao, "descrição", 500);
        this.preco = validarPreco(preco);
        this.disponivelApenasNoLocal = disponivelApenasNoLocal;
        this.caminhoFoto = caminhoFoto; // opcional
        this.restaurante = Validador.objetoObrigatorio(restaurante, "restaurante");
    }

    private BigDecimal validarPreco(BigDecimal preco) {
        Validador.objetoObrigatorio(preco, "preço");
        if (preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidacaoException("O preço deve ser maior que zero");
        }
        return preco;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public boolean isDisponivelApenasNoLocal() {
        return disponivelApenasNoLocal;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }
}
