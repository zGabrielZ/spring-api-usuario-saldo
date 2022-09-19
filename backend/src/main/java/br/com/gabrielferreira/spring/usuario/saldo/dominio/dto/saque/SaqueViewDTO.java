package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SaqueViewDTO (

        @ApiModelProperty(value = "Código do saque atual", example = "1")
        Long id,

        @ApiModelProperty(value = "Data do saque", example = "26/06/2022 12:00:00")
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime dataSaque,

        @ApiModelProperty(value = "Valor saque do usuário", example = "500.00")
        BigDecimal valor
) {

}
