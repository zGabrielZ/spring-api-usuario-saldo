package br.com.gabrielferreira.spring.usuario.saldo.controller;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.situacao.SituacaoViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.service.SituacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/situacoes")
@RequiredArgsConstructor
public class SituacaoController {

    private final SituacaoService situacaoService;

    @GetMapping("/{id}")
    public ResponseEntity<SituacaoViewDTO> buscarPorId(@PathVariable Long id){
        SituacaoViewDTO situacao = situacaoService.buscarPorId(id);
        return ResponseEntity.ok().body(situacao);
    }

    @GetMapping
    public ResponseEntity<List<SituacaoViewDTO>> listagem(){
        List<SituacaoViewDTO> situacoes = situacaoService.listagem();
        return ResponseEntity.ok().body(situacoes);
    }

}
