package dev.kauan.padroes.frete;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.kauan.padroes.dto.FreteResponse;
import dev.kauan.padroes.model.Cliente;

/** Contexto do Strategy: recebe todas as regras de frete e escolhe a primeira que atende a UF. */
@Service
public class FreteService {

    private final List<CalculoFreteStrategy> estrategias;

    public FreteService(List<CalculoFreteStrategy> estrategias) {
        this.estrategias = estrategias;
    }

    public FreteResponse calcular(Cliente cliente) {
        String uf = cliente.getEndereco().getUf();
        CalculoFreteStrategy estrategia = estrategias.stream()
                .filter(e -> e.atende(uf))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Nenhuma regra de frete para a UF " + uf));
        return new FreteResponse(cliente.getId(), uf, estrategia.nome(), estrategia.calcular());
    }
}
