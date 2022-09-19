package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import org.springframework.data.domain.Page;

import java.io.Serial;
import java.io.Serializable;

public class UsuarioDTOFactory implements Serializable {

    @Serial
    private static final long serialVersionUID = -4687700744639015221L;

    private UsuarioDTOFactory(){}

    public static UsuarioViewDTO toUsuarioViewDTO(Usuario usuario){
        return new UsuarioViewDTO(usuario.getId(),usuario.getNome(),usuario.getEmail(),usuario.getCpf(),usuario.getDataNascimento());
    }

    public static Page<UsuarioViewDTO> toPageUsuario(Page<Usuario> usuarios){
        return usuarios.map(UsuarioDTOFactory::toUsuarioViewDTO);
    }
}
