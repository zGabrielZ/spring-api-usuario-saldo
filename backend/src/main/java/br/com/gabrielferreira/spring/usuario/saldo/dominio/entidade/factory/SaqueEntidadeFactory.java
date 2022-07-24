package br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.factory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SacarFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saque;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;

import java.io.Serializable;
import java.time.LocalDateTime;

public class SaqueEntidadeFactory implements Serializable {

    private static final long serialVersionUID = 2762513171034400055L;

    private SaqueEntidadeFactory(){}

    public static Saque toSaqueInsertEntidade(SacarFormDTO sacarFormDTO){
        Usuario usuario = Usuario.builder().id(sacarFormDTO.getIdUsuario()).build();
        return Saque.builder().dataSaque(LocalDateTime.now()).valor(sacarFormDTO.getQuantidade())
                .usuario(usuario).build();
    }

}
