package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.UsuarioViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class UsuarioDTOFactory implements Serializable {

    private static final long serialVersionUID = -4687700744639015221L;

    public UsuarioViewDTO toUsuarioViewDTO(Usuario usuario){
        UsuarioViewDTO usuarioViewDTO = new UsuarioViewDTO();
        usuarioViewDTO.setId(usuario.getId());
        usuarioViewDTO.setNome(usuario.getNome());
        usuarioViewDTO.setEmail(usuario.getEmail());
        usuarioViewDTO.setDataNascimento(usuario.getDataNascimento());
        usuarioViewDTO.setCpf(usuario.getCpf());
        return usuarioViewDTO;
    }
}
