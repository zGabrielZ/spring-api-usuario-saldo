package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.pefil;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfilInsertFormDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -1708090859850629848L;

    @ApiModelProperty(value = "ID do perfil", example = "1")
    private Long id;
}
