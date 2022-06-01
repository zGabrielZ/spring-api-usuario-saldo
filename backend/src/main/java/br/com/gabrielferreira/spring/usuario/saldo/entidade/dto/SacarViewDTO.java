package br.com.gabrielferreira.spring.usuario.saldo.entidade.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SacarViewDTO implements Serializable {

    private static final long serialVersionUID = -7136821550686871414L;

    @ApiModelProperty(value = "Saldo total do usuário", example = "200.00")
    private BigDecimal saldoTotal;
}
