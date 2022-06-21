package br.com.gabrielferreira.spring.usuario.saldo.controller;

import br.com.gabrielferreira.spring.usuario.saldo.entidade.Saldo;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.dto.SaldoFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.dto.SaldoViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.service.SaldoService;
import br.com.gabrielferreira.spring.usuario.saldo.service.SaqueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/saldos")
@Api("Saldo API")
public class SaldoController {

    private final SaldoService saldoService;

    private final SaqueService saqueService;
    public SaldoController(SaldoService saldoService, SaqueService saqueService) {
        this.saldoService = saldoService;
        this.saqueService = saqueService;
    }

    @ApiOperation("Inserir um saldo para o usuário")
    @ResponseStatus(code = HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201,message = "Inseriu um déposito para o usuário"),
            @ApiResponse(code = 400,message = "Ocorreu um erro personalizado"),
            @ApiResponse(code = 404,message = "Usuário não foi encontrado")
    })
    @PostMapping("/depositar")
    public ResponseEntity<SaldoViewDTO> depositar(@Valid @RequestBody SaldoFormDTO saldoFormDTO, UriComponentsBuilder uriComponentsBuilder){
        Saldo saldo = saldoService.depositar(saldoFormDTO);
        URI uri = uriComponentsBuilder.path("/saldos/{id}").buildAndExpand(saldo.getId()).toUri();
        return ResponseEntity.created(uri).body(new SaldoViewDTO(saldo));
    }

}
