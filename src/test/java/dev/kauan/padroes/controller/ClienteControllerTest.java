package dev.kauan.padroes.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import dev.kauan.padroes.exception.ClienteNaoEncontradoException;
import dev.kauan.padroes.facade.CadastroClienteFacade;
import dev.kauan.padroes.frete.FreteService;
import dev.kauan.padroes.model.Cliente;
import dev.kauan.padroes.model.Endereco;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired MockMvc mvc;
    @MockBean CadastroClienteFacade facade;
    @MockBean FreteService freteService;

    @Test
    void post_comCepInvalido_retorna400ComDetalhesDoCampo() throws Exception {
        mvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Kauan\",\"cep\":\"123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.cep").exists());
    }

    @Test
    void get_comIdInexistente_retorna404() throws Exception {
        when(facade.buscar(99L)).thenThrow(new ClienteNaoEncontradoException(99L));

        mvc.perform(get("/clientes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void post_valido_retorna201ComLocation() throws Exception {
        Cliente cliente = new Cliente("Kauan",
                new Endereco("64000000", "Rua A", "Centro", "Teresina", "PI"));
        cliente.setId(1L);
        when(facade.criar(any())).thenReturn(cliente);

        mvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Kauan\",\"cep\":\"64000-000\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.endereco.uf").value("PI"));
    }
}
