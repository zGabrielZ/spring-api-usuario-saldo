package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Data
public class SaldoInsertResponseDTO extends SaldoInsertFormDTO {

    @Serial
    private static final long serialVersionUID = -2398120816295097002L;

    @ApiModelProperty(value = "Código do Saldo", example = "1")
    private Long id;

}
