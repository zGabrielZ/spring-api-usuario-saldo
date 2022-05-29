package br.com.gabrielferreira.spring.usuario.saldo.service;

import br.com.gabrielferreira.spring.usuario.saldo.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.dto.UsuarioFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.UsuarioRepositorio;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepositorio usuarioRepositorio;

    public UsuarioService(UsuarioRepositorio usuarioRepositorio){
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public Usuario inserir(UsuarioFormDTO usuarioFormDTO){
        Usuario usuario = new Usuario(null,usuarioFormDTO.getNome(), usuarioFormDTO.getEmail()
                , usuarioFormDTO.getSenha(), usuarioFormDTO.getCpf(), usuarioFormDTO.getDataNascimento());
        verificarEmail(usuario.getEmail());
        verificarCpf(usuario.getCpf());
        return usuarioRepositorio.save(usuario);
    }

    private void verificarEmail(String email){
        Optional<Usuario> optionalUsuario = usuarioRepositorio.findByEmail(email);
        if(optionalUsuario.isPresent()){
            throw new ExcecaoPersonalizada("Este e-mail já foi cadastrado.");
        }
    }

    private void verificarCpf(String cpf){
        Optional<Usuario> optionalUsuario = usuarioRepositorio.findByCpf(cpf);
        if(optionalUsuario.isPresent()){
            throw new ExcecaoPersonalizada("Este CPF já foi cadastrado.");
        }
    }

}
