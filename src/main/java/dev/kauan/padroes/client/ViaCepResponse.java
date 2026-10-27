package dev.kauan.padroes.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Resposta do ViaCEP. Para CEP bem formado, mas inexistente, a API responde
 * HTTP 200 com {"erro": true}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ViaCepResponse(
        String cep,
        String logradouro,
        String bairro,
        String localidade,
        String uf,
        Boolean erro) {

    public boolean inexistente() {
        return Boolean.TRUE.equals(erro);
    }
          }
