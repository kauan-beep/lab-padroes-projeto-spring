package dev.kauan.padroes.exception;

public class CepNaoEncontradoException extends RuntimeException {

    public CepNaoEncontradoException(String cep) {
        super("CEP " + cep + " não encontrado no ViaCEP");
    }
}
