package dev.kauan.padroes.exception;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import feign.FeignException;

/** Traduz exceções em respostas HTTP padronizadas (RFC 7807 / ProblemDetail). */
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ClienteNaoEncontradoException.class)
    ProblemDetail clienteNaoEncontrado(ClienteNaoEncontradoException ex) {
        ProblemDetail p = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        p.setTitle("Cliente não encontrado");
        return p;
    }

    @ExceptionHandler(CepNaoEncontradoException.class)
    ProblemDetail cepNaoEncontrado(CepNaoEncontradoException ex) {
        ProblemDetail p = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        p.setTitle("CEP não encontrado");
        return p;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail validacao(MethodArgumentNotValidException ex) {
        Map<String, String> campos = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        e -> Objects.requireNonNullElse(e.getDefaultMessage(), "inválido"),
                        (a, b) -> a));
        ProblemDetail p = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Dados inválidos");
        p.setTitle("Erro de validação");
        p.setProperty("campos", campos);
        return p;
    }

    @ExceptionHandler(FeignException.class)
    ProblemDetail falhaViaCep(FeignException ex) {
        ProblemDetail p = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_GATEWAY,
                "Falha ao consultar o ViaCEP. Tente novamente mais tarde.");
        p.setTitle("Serviço externo indisponível");
        return p;
    }
}
