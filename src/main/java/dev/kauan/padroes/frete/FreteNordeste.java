package dev.kauan.padroes.frete;

import java.math.BigDecimal;
import java.util.Set;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class FreteNordeste implements CalculoFreteStrategy {

    private static final Set<String> UFS = Set.of("MA", "PI", "CE", "RN", "PB", "PE", "AL", "SE", "BA");

    @Override
    public String nome() {
        return "NORDESTE";
    }

    @Override
    public boolean atende(String uf) {
        return uf != null && UFS.contains(uf);
    }

    @Override
    public BigDecimal calcular() {
        return new BigDecimal("15.00");
    }
}
