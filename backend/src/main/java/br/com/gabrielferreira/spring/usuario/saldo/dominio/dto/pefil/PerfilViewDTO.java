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
public class PerfilViewDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -6397866892357335927L;

    @ApiModelProperty(value = "ID do perfil", example = "1")
    private Long id;

    @ApiModelProperty(value = "Descrição do perfil", example = "Admin")
    private String descricao;
}
