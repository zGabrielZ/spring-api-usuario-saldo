package br.com.gabrielferreira.spring.usuario.saldo.client;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.FeriadoNacionalDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "feriadosNacionais", url = "https://brasilapi.com.br")
public interface FeriadoNacionalClient {

    @GetMapping("/api/feriados/v1/2022")
    List<FeriadoNacionalDTO> buscarFeriadosNacionais();
}
