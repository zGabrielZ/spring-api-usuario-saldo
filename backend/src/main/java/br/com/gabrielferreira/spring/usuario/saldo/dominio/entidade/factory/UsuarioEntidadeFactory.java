package br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.factory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.UsuarioInsertFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;

@Component
public class UsuarioEntidadeFactory implements Serializable {

    private static final long serialVersionUID = 2762513171034400055L;

    public Usuario toUsuarioInsertEntidade(UsuarioInsertFormDTO usuarioInsertFormDTO){
        Usuario usuario = new Usuario();
        usuario.setNome(usuarioInsertFormDTO.getNome());
        usuario.setEmail(usuarioInsertFormDTO.getEmail());
        usuario.setSenha(usuarioInsertFormDTO.getSenha());
        usuario.setCpf(usuarioInsertFormDTO.getCpf());
        usuario.setDataNascimento(usuarioInsertFormDTO.getDataNascimento());
        usuario.setSaldoTotal(BigDecimal.ZERO);
        return usuario;
    }
}
