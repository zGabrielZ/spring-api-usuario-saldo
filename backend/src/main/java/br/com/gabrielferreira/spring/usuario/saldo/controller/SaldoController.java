package br.com.gabrielferreira.spring.usuario.saldo.controller;

import br.com.gabrielferreira.spring.usuario.saldo.entidade.Saldo;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.dto.SaldoFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.dto.SaldoViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.service.SaldoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/saldos")
public class SaldoController {

    private final SaldoService saldoService;

    public SaldoController(SaldoService saldoService) {
        this.saldoService = saldoService;
    }

    @PostMapping("/depositar")
    public ResponseEntity<SaldoViewDTO> depositar(@Valid @RequestBody SaldoFormDTO saldoFormDTO, UriComponentsBuilder uriComponentsBuilder){
        Saldo saldo = saldoService.depositar(saldoFormDTO);
        URI uri = uriComponentsBuilder.path("/saldos/{id}").buildAndExpand(saldo.getId()).toUri();
        return ResponseEntity.created(uri).body(new SaldoViewDTO(saldo));
    }
}
