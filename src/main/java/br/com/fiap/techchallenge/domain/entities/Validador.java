package br.com.fiap.techchallenge.domain.entities;

import br.com.fiap.techchallenge.domain.exception.ValidacaoException;


public final class Validador {

    private Validador() {
        
    }

    public static String textoObrigatorio(String valor, String nomeDoCampo, int tamanhoMaximo) {
        if (valor == null || valor.isBlank()) {
            throw new ValidacaoException("O campo " + nomeDoCampo + " é obrigatório");
        }
        String valorSemEspacos = valor.trim();
        if (valorSemEspacos.length() > tamanhoMaximo) {
            throw new ValidacaoException("O campo " + nomeDoCampo + " deve ter no máximo " + tamanhoMaximo + " caracteres");
        }
        return valorSemEspacos;
    }

    public static <T> T objetoObrigatorio(T valor, String nomeDoCampo) {
        if (valor == null) {
            throw new ValidacaoException("O campo " + nomeDoCampo + " é obrigatório");
        }
        return valor;
    }
}
