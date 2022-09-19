package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaldoTotalViewDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -2398120816295097002L;

    @ApiModelProperty(value = "Saldo total do usuário", example = "500.00")
    private BigDecimal saldoTotal;

}
