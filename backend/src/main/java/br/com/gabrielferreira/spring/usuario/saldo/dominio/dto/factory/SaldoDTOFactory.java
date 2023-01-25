package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoInsertResponseDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoTotalViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saldo;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

public class SaldoDTOFactory implements Serializable {

    @Serial
    private static final long serialVersionUID = -4687700744639015221L;

    private SaldoDTOFactory(){}

    public static SaldoInsertResponseDTO toSaldoInsertResonseDTO(Saldo saldo){
        SaldoInsertResponseDTO saldoInsertResponseDTO = new SaldoInsertResponseDTO();
        saldoInsertResponseDTO.setId(saldo.getId());
        saldoInsertResponseDTO.setDeposito(saldo.getDeposito());
        saldoInsertResponseDTO.setIdUsuario(saldo.getUsuario().getId());
        return saldoInsertResponseDTO;
    }

    public static SaldoTotalViewDTO toSaldoTotalViewDTO(BigDecimal saldoTotal){
        return SaldoTotalViewDTO.builder().saldoTotal(saldoTotal).build();
    }
}
