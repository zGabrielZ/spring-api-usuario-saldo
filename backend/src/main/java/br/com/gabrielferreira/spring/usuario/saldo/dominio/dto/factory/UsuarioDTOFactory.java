package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import org.springframework.data.domain.Page;

import java.io.Serializable;

public class UsuarioDTOFactory implements Serializable {

    private static final long serialVersionUID = -4687700744639015221L;

    private UsuarioDTOFactory(){}

    public static UsuarioViewDTO toUsuarioViewDTO(Usuario usuario){
        UsuarioViewDTO usuarioViewDTO = new UsuarioViewDTO();
        usuarioViewDTO.setId(usuario.getId());
        usuarioViewDTO.setNome(usuario.getNome());
        usuarioViewDTO.setEmail(usuario.getEmail());
        usuarioViewDTO.setDataNascimento(usuario.getDataNascimento());
        usuarioViewDTO.setCpf(usuario.getCpf());
        return usuarioViewDTO;
    }

    public static Page<UsuarioViewDTO> toPageUsuario(Page<Usuario> usuarios){
        return usuarios.map(UsuarioDTOFactory::toUsuarioViewDTO);
    }
}
