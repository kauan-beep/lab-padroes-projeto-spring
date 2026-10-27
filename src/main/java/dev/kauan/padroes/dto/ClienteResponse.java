package dev.kauan.padroes.dto;

import dev.kauan.padroes.model.Cliente;

public record ClienteResponse(Long id, String nome, EnderecoResponse endereco) {

    public static ClienteResponse from(Cliente c) {
        return new ClienteResponse(c.getId(), c.getNome(), EnderecoResponse.from(c.getEndereco()));
    }
}
