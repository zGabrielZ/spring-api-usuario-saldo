package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoTotalViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saldo;

import java.io.Serializable;
import java.math.BigDecimal;

public class SaldoDTOFactory implements Serializable {

    private static final long serialVersionUID = -4687700744639015221L;

    private SaldoDTOFactory(){}

    public static SaldoViewDTO toSaldoViewDTO(Saldo saldo){
        return new SaldoViewDTO(saldo.getId(), saldo.getDeposito(), saldo.getDataDeposito());
    }

    public static SaldoTotalViewDTO toSaldoTotalViewDTO(BigDecimal saldoTotal){
        return SaldoTotalViewDTO.builder().saldoTotal(saldoTotal).build();
    }
}
