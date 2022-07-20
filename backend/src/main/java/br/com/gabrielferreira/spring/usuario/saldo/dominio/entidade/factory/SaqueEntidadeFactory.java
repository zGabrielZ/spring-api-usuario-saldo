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
        Saque saque = new Saque();
        saque.setDataSaque(LocalDateTime.now());
        saque.setValor(sacarFormDTO.getQuantidade());

        Usuario usuario = new Usuario();
        usuario.setId(sacarFormDTO.getIdUsuario());

        saque.setUsuario(usuario);
        return saque;
    }

}
