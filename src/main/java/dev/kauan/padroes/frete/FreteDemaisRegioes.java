package dev.kauan.padroes.frete;

import java.math.BigDecimal;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/** Regra padrao: atende qualquer UF e por isso deve ser sempre a ultima avaliada. */
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class FreteDemaisRegioes implements CalculoFreteStrategy {

    @Override
    public String nome() {
        return "DEMAIS_REGIOES";
    }

    @Override
    public boolean atende(String uf) {
        return true;
    }

    @Override
    public BigDecimal calcular() {
        return new BigDecimal("40.00");
    }
}
