package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
public class SaldoTotalViewDTO implements Serializable {

    private static final long serialVersionUID = -2398120816295097002L;

    @ApiModelProperty(value = "Saldo total do usuário", example = "500.00")
    private BigDecimal saldoTotal;

    public SaldoTotalViewDTO(BigDecimal saldoTotal){
        this.saldoTotal = saldoTotal != null ? saldoTotal : BigDecimal.ZERO;
    }
}
