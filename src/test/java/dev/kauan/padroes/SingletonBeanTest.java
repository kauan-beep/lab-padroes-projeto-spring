package dev.kauan.padroes;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import dev.kauan.padroes.facade.CadastroClienteFacade;
import dev.kauan.padroes.frete.FreteService;

/** Demonstra o Singleton do Spring: o container devolve sempre a mesma instancia do bean. */
@SpringBootTest
class SingletonBeanTest {

    @Autowired ApplicationContext contexto;
    @Autowired CadastroClienteFacade facadeInjetada;

    @Test
    void facade_eSempreAMesmaInstancia() {
        CadastroClienteFacade primeira = contexto.getBean(CadastroClienteFacade.class);
        CadastroClienteFacade segunda = contexto.getBean(CadastroClienteFacade.class);

        assertThat(primeira).isSameAs(segunda).isSameAs(facadeInjetada);
    }

    @Test
    void freteService_eSempreAMesmaInstancia() {
        assertThat(contexto.getBean(FreteService.class)).isSameAs(contexto.getBean(FreteService.class));
    }
}
