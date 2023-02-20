package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;


public record SaldoViewDTO (
        @ApiModelProperty(value = "Código do saldo atual", example = "1")
        Long id,

        @ApiModelProperty(value = "Saldo total do usuário", example = "500.00")
        BigDecimal deposito,

        @ApiModelProperty(value = "Data do déposito", example = "26/06/2022 12:00:00")
        LocalDateTime data
) {

}
