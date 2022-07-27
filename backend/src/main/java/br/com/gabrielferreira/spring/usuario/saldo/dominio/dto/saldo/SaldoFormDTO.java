package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaldoFormDTO implements Serializable {

    private static final long serialVersionUID = -2398120816295097002L;

    @ApiModelProperty(value = "Valor do déposito", example = "500.00")
    @NotNull(message = "Déposito não pode ser vazio.")
    private BigDecimal deposito;

    @ApiModelProperty(value = "ID do usuário", example = "1")
    @NotNull(message = "Usuário não pode ser vazio.")
    private Long idUsuario;

}
