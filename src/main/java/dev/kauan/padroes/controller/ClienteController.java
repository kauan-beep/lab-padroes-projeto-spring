package dev.kauan.padroes.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dev.kauan.padroes.dto.ClienteRequest;
import dev.kauan.padroes.dto.ClienteResponse;
import dev.kauan.padroes.dto.FreteResponse;
import dev.kauan.padroes.facade.CadastroClienteFacade;
import dev.kauan.padroes.frete.FreteService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final CadastroClienteFacade facade;
    private final FreteService freteService;

    public ClienteController(CadastroClienteFacade facade, FreteService freteService) {
        this.facade = facade;
        this.freteService = freteService;
    }

    @GetMapping
    public List<ClienteResponse> listar() {
        return facade.listar().stream().map(ClienteResponse::from).toList();
    }

    @GetMapping("/{id}")
    public ClienteResponse buscar(@PathVariable Long id) {
        return ClienteResponse.from(facade.buscar(id));
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> criar(@Valid @RequestBody ClienteRequest request) {
        ClienteResponse criado = ClienteResponse.from(facade.criar(request));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @PutMapping("/{id}")
    public ClienteResponse atualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequest request) {
        return ClienteResponse.from(facade.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        facade.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/frete")
    public FreteResponse frete(@PathVariable Long id) {
        return freteService.calcular(facade.buscar(id));
    }
}
