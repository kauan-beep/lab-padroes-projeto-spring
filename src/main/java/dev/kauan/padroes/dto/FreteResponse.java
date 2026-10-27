package dev.kauan.padroes.dto;

import java.math.BigDecimal;

public record FreteResponse(Long clienteId, String uf, String estrategia, BigDecimal valor) {
}
