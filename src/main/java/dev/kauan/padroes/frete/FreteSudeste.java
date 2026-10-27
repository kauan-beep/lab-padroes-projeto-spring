package dev.kauan.padroes.frete;

import java.math.BigDecimal;
import java.util.Set;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class FreteSudeste implements CalculoFreteStrategy {

    private static final Set<String> UFS = Set.of("SP", "RJ", "MG", "ES");

    @Override
    public String nome() {
        return "SUDESTE";
    }

    @Override
    public boolean atende(String uf) {
        return uf != null && UFS.contains(uf);
    }

    @Override
    public BigDecimal calcular() {
        return new BigDecimal("25.00");
    }
}
