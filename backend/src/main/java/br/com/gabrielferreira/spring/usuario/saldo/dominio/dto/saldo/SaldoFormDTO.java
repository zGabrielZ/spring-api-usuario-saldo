package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaldoFormDTO implements Serializable {

    private static final long serialVersionUID = -2398120816295097002L;

    @ApiModelProperty(value = "Valor do déposito", example = "500.00")
    @NotNull(message = "Déposito não pode ser vazio.")
    private BigDecimal deposito;

    @ApiModelProperty(value = "Data do déposito", example = "26/06/2022 12:00:00")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @NotNull(message = "Data do déposito não pode ser vazio.")
    @Future(message = "Data de déposito não pode ser passada.")
    private LocalDateTime dataDeposito;

    @ApiModelProperty(value = "ID do usuário", example = "1")
    @NotNull(message = "Usuário não pode ser vazio.")
    private Long idUsuario;

}
