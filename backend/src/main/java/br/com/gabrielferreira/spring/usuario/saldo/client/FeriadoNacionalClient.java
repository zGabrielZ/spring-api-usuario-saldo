package br.com.gabrielferreira.spring.usuario.saldo.client;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.FeriadoNacionalDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "feriadosNacionais", url = "${feriados.nacionais.url}")
public interface FeriadoNacionalClient {

    @GetMapping("/api/feriados/v1/{ano}")
    List<FeriadoNacionalDTO> buscarFeriadosNacionais(@PathVariable int ano);
}
