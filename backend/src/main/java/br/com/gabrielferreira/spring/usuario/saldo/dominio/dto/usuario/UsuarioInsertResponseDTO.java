package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Data
public class UsuarioInsertResponseDTO extends UsuarioInsertFormDTO {

    @Serial
    private static final long serialVersionUID = 5649141200664469763L;

    @ApiModelProperty(value = "Código do usuário", example = "1")
    private Long id;
}
