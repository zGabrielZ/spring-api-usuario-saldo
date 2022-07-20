package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaldoViewDTO implements Serializable {

    private static final long serialVersionUID = -2398120816295097002L;

    @ApiModelProperty(value = "Código do saldo atual", example = "1")
    private Long id;

    @ApiModelProperty(value = "Saldo total do usuário", example = "500.00")
    private BigDecimal deposito;

    @ApiModelProperty(value = "Data do déposito", example = "26/06/2022 12:00:00")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataDeposito;

}
