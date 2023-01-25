package br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.factory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoInsertFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saldo;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;

public class SaldoEntidadeFactory implements Serializable {

    @Serial
    private static final long serialVersionUID = 2762513171034400055L;

    private SaldoEntidadeFactory(){}

    public static Saldo toSaldoInsertEntidade(SaldoInsertFormDTO saldoInsertFormDTO, ZonedDateTime dataDeposito, Usuario usuarioEncontrado, Usuario usuarioLogado){
        return Saldo.builder().deposito(saldoInsertFormDTO.getDeposito()).dataDeposito(dataDeposito)
                .usuario(usuarioEncontrado).usuarioDepositante(usuarioLogado).build();
    }

}
