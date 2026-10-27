package dev.kauan.padroes.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

import dev.kauan.padroes.client.ViaCepClient;

/**
 * Habilita os clients Feign em uma classe separada da {@code Application}.
 * Assim os testes de fatia (como {@code @WebMvcTest}) nao tentam criar os clients.
 */
@Configuration
@EnableFeignClients(basePackageClasses = ViaCepClient.class)
public class FeignConfig {
}
