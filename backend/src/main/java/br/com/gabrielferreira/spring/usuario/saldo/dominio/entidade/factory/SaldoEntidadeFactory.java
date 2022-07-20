package br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.factory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saldo;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import java.io.Serializable;

public class SaldoEntidadeFactory implements Serializable {

    private static final long serialVersionUID = 2762513171034400055L;

    private SaldoEntidadeFactory(){}

    public static Saldo toSaldoInsertEntidade(SaldoFormDTO saldoFormDTO){
        Saldo saldo = new Saldo();
        saldo.setDeposito(saldoFormDTO.getDeposito());
        saldo.setDataDeposito(saldoFormDTO.getDataDeposito());

        Usuario usuario = new Usuario();
        usuario.setId(saldoFormDTO.getIdUsuario());

        saldo.setUsuario(usuario);
        return saldo;
    }

}
