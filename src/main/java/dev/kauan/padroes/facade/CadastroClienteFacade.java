package dev.kauan.padroes.facade;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.kauan.padroes.client.ViaCepClient;
import dev.kauan.padroes.client.ViaCepResponse;
import dev.kauan.padroes.dto.ClienteRequest;
import dev.kauan.padroes.exception.CepNaoEncontradoException;
import dev.kauan.padroes.exception.ClienteNaoEncontradoException;
import dev.kauan.padroes.model.Cliente;
import dev.kauan.padroes.model.Endereco;
import dev.kauan.padroes.repository.ClienteRepository;
import dev.kauan.padroes.repository.EnderecoRepository;

/**
 * FACADE: expoe uma interface simples (criar, atualizar, buscar...) e esconde
 * a coordenacao entre tres subsistemas: repositorio de clientes, repositorio
 * de enderecos (cache local) e a API externa do ViaCEP.
 *
 * SINGLETON: por ser um {@code @Service}, o Spring cria uma unica instancia
 * dele para toda a aplicacao.
 */
@Service
public class CadastroClienteFacade {

    private final ClienteRepository clienteRepository;
    private final EnderecoRepository enderecoRepository;
    private final ViaCepClient viaCepClient;

    public CadastroClienteFacade(ClienteRepository clienteRepository,
                                 EnderecoRepository enderecoRepository,
                                 ViaCepClient viaCepClient) {
        this.clienteRepository = clienteRepository;
        this.enderecoRepository = enderecoRepository;
        this.viaCepClient = viaCepClient;
    }

    @Transactional(readOnly = true)
    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Cliente buscar(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException(id));
    }

    @Transactional
    public Cliente criar(ClienteRequest request) {
        Endereco endereco = resolverEndereco(request.cep());
        return clienteRepository.save(new Cliente(request.nome(), endereco));
    }

    @Transactional
    public Cliente atualizar(Long id, ClienteRequest request) {
        Cliente cliente = buscar(id);
        cliente.setNome(request.nome());
        cliente.setEndereco(resolverEndereco(request.cep()));
        return clienteRepository.save(cliente);
    }

    @Transactional
    public void deletar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ClienteNaoEncontradoException(id);
        }
        clienteRepository.deleteById(id);
    }

    /** Usa o endereco ja salvo; se nao existir, consulta o ViaCEP e persiste. */
    private Endereco resolverEndereco(String cepInformado) {
        String cep = cepInformado.replace("-", "");
        return enderecoRepository.findById(cep)
                .orElseGet(() -> buscarNoViaCep(cep));
    }

    private Endereco buscarNoViaCep(String cep) {
        ViaCepResponse resposta = viaCepClient.consultar(cep);
        if (resposta == null || resposta.inexistente()) {
            throw new CepNaoEncontradoException(cep);
        }
        Endereco novo = new Endereco(cep, resposta.logradouro(), resposta.bairro(),
                resposta.localidade(), resposta.uf());
        return enderecoRepository.save(novo);
    }
}
