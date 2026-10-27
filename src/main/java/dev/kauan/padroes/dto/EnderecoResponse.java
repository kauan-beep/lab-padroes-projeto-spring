package dev.kauan.padroes.dto;

import dev.kauan.padroes.model.Endereco;

public record EnderecoResponse(String cep, String logradouro, String bairro, String localidade, String uf) {

    public static EnderecoResponse from(Endereco e) {
        return new EnderecoResponse(e.getCep(), e.getLogradouro(), e.getBairro(), e.getLocalidade(), e.getUf());
    }
}
