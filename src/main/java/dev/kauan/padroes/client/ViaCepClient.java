package dev.kauan.padroes.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/** Client HTTP (OpenFeign) para a API publica do ViaCEP. */
@FeignClient(name = "viacep", url = "${viacep.url}")
public interface ViaCepClient {

    @GetMapping("/{cep}/json/")
    ViaCepResponse consultar(@PathVariable("cep") String cep);
}
