package br.com.gabrielferreira.spring.usuario.saldo.service;

import br.com.gabrielferreira.spring.usuario.saldo.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.dto.UsuarioFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.UsuarioRepositorio;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepositorio usuarioRepositorio;

    public UsuarioService(UsuarioRepositorio usuarioRepositorio){
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public Usuario inserir(UsuarioFormDTO usuarioFormDTO){
        Usuario usuario = new Usuario(null,usuarioFormDTO.getNome(), usuarioFormDTO.getEmail()
                , usuarioFormDTO.getSenha(), usuarioFormDTO.getCpf(), usuarioFormDTO.getDataNascimento());
        return usuarioRepositorio.save(usuario);
    }

}
