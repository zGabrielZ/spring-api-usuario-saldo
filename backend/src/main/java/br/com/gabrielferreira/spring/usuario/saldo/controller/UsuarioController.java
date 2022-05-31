package br.com.gabrielferreira.spring.usuario.saldo.controller;

import br.com.gabrielferreira.spring.usuario.saldo.entidade.Saldo;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.dto.SaldoViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.dto.UsuarioFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.dto.UsuarioUpdateDTO;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.dto.UsuarioViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import br.com.gabrielferreira.spring.usuario.saldo.service.SaldoService;
import br.com.gabrielferreira.spring.usuario.saldo.service.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    private final SaldoService saldoService;

    public UsuarioController(UsuarioService usuarioService, SaldoService saldoService) {
        this.usuarioService = usuarioService;
        this.saldoService = saldoService;
    }

    @PostMapping
    public ResponseEntity<UsuarioViewDTO> inserir(@Valid @RequestBody UsuarioFormDTO usuarioFormDTO, UriComponentsBuilder uriComponentsBuilder){
        Usuario usuario = usuarioService.inserir(usuarioFormDTO);
        URI uri = uriComponentsBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(new UsuarioViewDTO(usuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioViewDTO> buscarPorId(@PathVariable Long id){
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok().body(new UsuarioViewDTO(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable Long id){
        usuarioService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioViewDTO> atualizarDados(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateDTO usuarioUpdateDTO){
        Usuario usuario = usuarioService.atualizar(id,usuarioUpdateDTO);
        return ResponseEntity.ok().body(new UsuarioViewDTO(usuario));
    }

    @GetMapping
    public ResponseEntity<Page<UsuarioViewDTO>> listagem(
            @RequestParam(value = "pagina", required = false, defaultValue = "0") Integer pagina,
            @RequestParam(value = "quantidadeRegistro", required = false, defaultValue = "5") Integer quantidadeRegistro,
            @RequestParam(value = "direcao", required = false, defaultValue = "ASC") String direcao,
            @RequestParam(value = "ordenar", required = false, defaultValue = "nome") String ordenar
    ){
        Optional<Sort.Direction> optionalDirecao = Sort.Direction.fromOptionalString(direcao);
        if(optionalDirecao.isEmpty()){
            throw new ExcecaoPersonalizada("A direção informada está incorreta, informe DESC ou ASC");
        }

        PageRequest pageRequest = PageRequest.of(pagina,quantidadeRegistro, optionalDirecao.get(),ordenar);
        Page<Usuario> usuarios = usuarioService.listagem(pageRequest);
        return ResponseEntity.ok().body(UsuarioViewDTO.converterParaDto(usuarios));
    }

    @GetMapping("/saldos/{id}")
    public ResponseEntity<List<SaldoViewDTO>> listaDeSaldosPorUsuario(@PathVariable Long id){
        List<Saldo> saldos = saldoService.saldosPorUsuario(id);
        return ResponseEntity.ok().body(SaldoViewDTO.listParaDto(saldos));
    }

    @GetMapping("/saldo-total/{id}")
    public ResponseEntity<BigDecimal> saldoTotalPorUsuario(@PathVariable Long id){
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok().body(usuario.getSaldoTotal());
    }
}
