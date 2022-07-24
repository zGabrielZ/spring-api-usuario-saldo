package br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.factory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioInsertFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioUpdateFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;

import java.io.Serializable;
import java.math.BigDecimal;

public class UsuarioEntidadeFactory implements Serializable {

    private static final long serialVersionUID = 2762513171034400055L;

    private UsuarioEntidadeFactory(){}

    public static Usuario toUsuarioInsertEntidade(UsuarioInsertFormDTO usuarioInsertFormDTO){
        return Usuario.builder().nome(usuarioInsertFormDTO.getNome()).email(usuarioInsertFormDTO.getEmail())
                .senha(usuarioInsertFormDTO.getSenha()).cpf(usuarioInsertFormDTO.getCpf()).dataNascimento(usuarioInsertFormDTO.getDataNascimento())
                .saldoTotal(BigDecimal.ZERO).build();
    }

    public static Usuario toUsuarioUpdateEntidade(UsuarioUpdateFormDTO usuarioUpdateFormDTO, Usuario usuario){
        usuario.setSenha(usuarioUpdateFormDTO.getSenha());
        usuario.setNome(usuarioUpdateFormDTO.getNome());
        usuario.setEmail(usuarioUpdateFormDTO.getEmail());
        usuario.setDataNascimento(usuarioUpdateFormDTO.getDataNascimento());
        return usuario;
    }
}
