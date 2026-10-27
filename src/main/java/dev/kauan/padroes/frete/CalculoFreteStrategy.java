package dev.kauan.padroes.frete;

import java.math.BigDecimal;

/**
 * STRATEGY: cada implementacao e uma regra de frete. O {@link FreteService}
 * escolhe a regra certa em tempo de execucao, conforme a UF do cliente.
 * Para criar uma nova regra, basta criar um novo {@code @Component}.
 */
public interface CalculoFreteStrategy {

    String nome();

    boolean atende(String uf);

    BigDecimal calcular();
}
