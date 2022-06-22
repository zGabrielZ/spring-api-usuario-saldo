package br.com.gabrielferreira.spring.usuario.saldo.entidade.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SacarFormDTO implements Serializable {

    private static final long serialVersionUID = -7136821550686871414L;

    @ApiModelProperty(value = "Valor do saque", example = "400.00")
    @NotNull(message = "É necessário informar a quantidade do saque.")
    private BigDecimal quantidade;

    @ApiModelProperty(value = "ID do usuário", example = "1")
    @NotNull(message = "É necessário informar o usuário.")
    private Long idUsuario;
}
