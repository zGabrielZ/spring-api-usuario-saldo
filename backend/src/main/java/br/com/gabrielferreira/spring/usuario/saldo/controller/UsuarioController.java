package br.com.gabrielferreira.spring.usuario.saldo.controller;

import br.com.gabrielferreira.spring.usuario.saldo.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.dto.UsuarioFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.dto.UsuarioViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioViewDTO> inserir(@Valid @RequestBody UsuarioFormDTO usuarioFormDTO){
        Usuario usuario = usuarioService.inserir(usuarioFormDTO);
        return ResponseEntity.ok().body(new UsuarioViewDTO(usuario));
    }
}
