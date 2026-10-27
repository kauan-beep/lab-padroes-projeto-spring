package dev.kauan.padroes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.kauan.padroes.model.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, String> {
}
