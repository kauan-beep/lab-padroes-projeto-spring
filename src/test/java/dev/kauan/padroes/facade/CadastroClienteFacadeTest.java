package dev.kauan.padroes.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.kauan.padroes.client.ViaCepClient;
import dev.kauan.padroes.client.ViaCepResponse;
import dev.kauan.padroes.dto.ClienteRequest;
import dev.kauan.padroes.exception.CepNaoEncontradoException;
import dev.kauan.padroes.exception.ClienteNaoEncontradoException;
import dev.kauan.padroes.model.Cliente;
import dev.kauan.padroes.model.Endereco;
import dev.kauan.padroes.repository.ClienteRepository;
import dev.kauan.padroes.repository.EnderecoRepository;

@ExtendWith(MockitoExtension.class)
class CadastroClienteFacadeTest {

    @Mock ClienteRepository clienteRepository;
    @Mock EnderecoRepository enderecoRepository;
    @Mock ViaCepClient viaCepClient;
    @InjectMocks CadastroClienteFacade facade;

    @Test
    void criar_comCepNovo_consultaViaCepEPersisteEndereco() {
        when(enderecoRepository.findById("64000000")).thenReturn(Optional.empty());
        when(viaCepClient.consultar("64000000")).thenReturn(
                new ViaCepResponse("64000-000", "Rua A", "Centro", "Teresina", "PI", null));
        when(enderecoRepository.save(any(Endereco.class))).thenAnswer(i -> i.getArgument(0));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(i -> i.getArgument(0));

        Cliente criado = facade.criar(new ClienteRequest("Kauan", "64000-000"));

        assertThat(criado.getNome()).isEqualTo("Kauan");
        assertThat(criado.getEndereco().getCep()).isEqualTo("64000000");
        assertThat(criado.getEndereco().getUf()).isEqualTo("PI");
        verify(viaCepClient).consultar("64000000");
    }

    @Test
    void criar_comCepJaSalvo_naoConsultaViaCep() {
        Endereco existente = new Endereco("64000000", "Rua A", "Centro", "Teresina", "PI");
        when(enderecoRepository.findById("64000000")).thenReturn(Optional.of(existente));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(i -> i.getArgument(0));

        Cliente criado = facade.criar(new ClienteRequest("Kauan", "64000000"));

        assertThat(criado.getEndereco()).isSameAs(existente);
        verifyNoInteractions(viaCepClient);
    }

    @Test
    void criar_comCepInexistente_lancaExcecaoENaoSalvaCliente() {
        when(enderecoRepository.findById("99999999")).thenReturn(Optional.empty());
        when(viaCepClient.consultar("99999999")).thenReturn(
                new ViaCepResponse(null, null, null, null, null, true));

        assertThrows(CepNaoEncontradoException.class,
                () -> facade.criar(new ClienteRequest("Kauan", "99999999")));

        verify(clienteRepository, never()).save(any());
    }

    @Test
    void atualizar_mantemOMesmoIdEAlteraOsDados() {
        Endereco endereco = new Endereco("64000000", "Rua A", "Centro", "Teresina", "PI");
        Cliente existente = new Cliente("Antigo", endereco);
        existente.setId(1L);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(enderecoRepository.findById("64000000")).thenReturn(Optional.of(endereco));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(i -> i.getArgument(0));

        Cliente atualizado = facade.atualizar(1L, new ClienteRequest("Novo Nome", "64000000"));

        assertThat(atualizado.getId()).isEqualTo(1L);
        assertThat(atualizado.getNome()).isEqualTo("Novo Nome");
    }

    @Test
    void buscar_comIdInexistente_lancaClienteNaoEncontrado() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ClienteNaoEncontradoException.class, () -> facade.buscar(99L));
    }

    @Test
    void deletar_comIdInexistente_lancaClienteNaoEncontrado() {
        when(clienteRepository.existsById(99L)).thenReturn(false);

        assertThrows(ClienteNaoEncontradoException.class, () -> facade.deletar(99L));
        verify(clienteRepository, never()).deleteById(any());
    }
    }
