package br.com.fiap.techchallenge.infrastructure.web.exception;

import br.com.fiap.techchallenge.domain.exception.NaoEncontradoException;
import br.com.fiap.techchallenge.domain.exception.RegraDeNegocioException;
import br.com.fiap.techchallenge.domain.exception.ValidacaoException;
import br.com.fiap.techchallenge.infrastructure.web.dto.ErroResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.List;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NaoEncontradoException.class)
    public ResponseEntity<ErroResponse> tratarNaoEncontrado(NaoEncontradoException ex) {
        return montarResposta(HttpStatus.NOT_FOUND, ex.getMessage(), List.of());
    }

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<ErroResponse> tratarValidacao(ValidacaoException ex) {
        return montarResposta(HttpStatus.BAD_REQUEST, ex.getMessage(), List.of());
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ErroResponse> tratarRegraDeNegocio(RegraDeNegocioException ex) {
        return montarResposta(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), List.of());
    }

    // erros das anotações @NotBlank, @NotNull etc. dos requests
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> tratarCamposInvalidos(MethodArgumentNotValidException ex) {
        List<String> detalhes = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .toList();
        return montarResposta(HttpStatus.BAD_REQUEST, "Existem campos inválidos na requisição", detalhes);
    }

    // JSON mal formatado (ex: esqueceu uma vírgula ou mandou texto no lugar de número)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> tratarJsonInvalido(HttpMessageNotReadableException ex) {
        return montarResposta(HttpStatus.BAD_REQUEST, "O corpo da requisição está inválido ou mal formatado", List.of());
    }

    // ex: chamar /usuarios/abc quando o id deveria ser número
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponse> tratarParametroInvalido(MethodArgumentTypeMismatchException ex) {
        return montarResposta(HttpStatus.BAD_REQUEST, "O parâmetro '" + ex.getName() + "' está com valor inválido", List.of());
    }

    private ResponseEntity<ErroResponse> montarResposta(HttpStatus status, String mensagem, List<String> detalhes) {
        ErroResponse erro = new ErroResponse(LocalDateTime.now(), status.value(), status.getReasonPhrase(), mensagem, detalhes);
        return ResponseEntity.status(status).body(erro);
    }
}
