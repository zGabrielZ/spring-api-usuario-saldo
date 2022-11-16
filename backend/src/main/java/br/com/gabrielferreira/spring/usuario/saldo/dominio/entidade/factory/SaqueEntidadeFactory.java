package br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.factory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SacarFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saque;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class SaqueEntidadeFactory implements Serializable {

    @Serial
    private static final long serialVersionUID = 2762513171034400055L;

    private SaqueEntidadeFactory(){}

    public static Saque toSaqueInsertEntidade(SacarFormDTO sacarFormDTO, LocalDateTime dataAtual){
        Usuario usuario = Usuario.builder().id(sacarFormDTO.getIdUsuario()).build();
        return Saque.builder().dataSaque(dataAtual).valor(sacarFormDTO.getQuantidade())
                .usuario(usuario).build();
    }

}
