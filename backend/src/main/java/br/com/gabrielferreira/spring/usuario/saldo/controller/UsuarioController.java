package br.com.gabrielferreira.spring.usuario.saldo.controller;

import br.com.gabrielferreira.spring.usuario.saldo.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.dto.UsuarioFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.dto.UsuarioUpdateDTO;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.dto.UsuarioViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
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
    public ResponseEntity<Void> deletarPorID(@PathVariable Long id){
        usuarioService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioViewDTO> atualizarDados(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateDTO usuarioUpdateDTO){
        Usuario usuario = usuarioService.atualizar(id,usuarioUpdateDTO);
        return ResponseEntity.ok().body(new UsuarioViewDTO(usuario));
    }
}
