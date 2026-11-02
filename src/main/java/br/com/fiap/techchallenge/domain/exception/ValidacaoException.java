package br.com.fiap.techchallenge.domain.exception;


public class ValidacaoException extends RuntimeException {

    public ValidacaoException(String mensagem) {
        super(mensagem);
    }
}
