package br.com.gabrielferreira.spring.usuario.saldo.entidade.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SaldoTotalViewDTO implements Serializable {

    private static final long serialVersionUID = -2398120816295097002L;

    private BigDecimal saldoTotal;

    public SaldoTotalViewDTO(BigDecimal saldoTotal){
        this.saldoTotal = saldoTotal != null ? saldoTotal : BigDecimal.ZERO;
    }
}
