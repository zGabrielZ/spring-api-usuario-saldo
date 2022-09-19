package br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.factory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saldo;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class SaldoEntidadeFactory implements Serializable {

    @Serial
    private static final long serialVersionUID = 2762513171034400055L;

    private SaldoEntidadeFactory(){}

    public static Saldo toSaldoInsertEntidade(SaldoFormDTO saldoFormDTO, LocalDateTime dataDeposito){
        Usuario usuario = Usuario.builder().id(saldoFormDTO.getIdUsuario()).build();
        return Saldo.builder().deposito(saldoFormDTO.getDeposito()).dataDeposito(dataDeposito)
                .usuario(usuario).build();
    }

}
