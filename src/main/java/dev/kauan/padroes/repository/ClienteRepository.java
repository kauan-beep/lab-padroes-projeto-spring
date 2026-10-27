package dev.kauan.padroes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.kauan.padroes.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
