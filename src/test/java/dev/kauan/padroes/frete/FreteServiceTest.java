package dev.kauan.padroes.frete;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

import dev.kauan.padroes.dto.FreteResponse;
import dev.kauan.padroes.model.Cliente;
import dev.kauan.padroes.model.Endereco;

class FreteServiceTest {

    private final FreteService service = new FreteService(
            List.of(new FreteNordeste(), new FreteSudeste(), new FreteDemaisRegioes()));

    private Cliente clienteDe(String uf) {
        Cliente c = new Cliente("Teste", new Endereco("00000000", "Rua", "Bairro", "Cidade", uf));
        c.setId(1L);
        return c;
    }

    @Test
    void ufDoNordeste_usaEstrategiaNordeste() {
        FreteResponse r = service.calcular(clienteDe("PI"));

        assertThat(r.estrategia()).isEqualTo("NORDESTE");
        assertThat(r.valor()).isEqualByComparingTo(new BigDecimal("15.00"));
    }

    @Test
    void ufDoSudeste_usaEstrategiaSudeste() {
        FreteResponse r = service.calcular(clienteDe("SP"));

        assertThat(r.estrategia()).isEqualTo("SUDESTE");
        assertThat(r.valor()).isEqualByComparingTo(new BigDecimal("25.00"));
    }

    @Test
    void outraUf_usaEstrategiaPadrao() {
        FreteResponse r = service.calcular(clienteDe("RS"));

        assertThat(r.estrategia()).isEqualTo("DEMAIS_REGIOES");
        assertThat(r.valor()).isEqualByComparingTo(new BigDecimal("40.00"));
    }
}
