package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.pefil;

import io.swagger.annotations.ApiModelProperty;

public record PerfilViewDTO(

        @ApiModelProperty(value = "ID do perfil", example = "1")
        Long id,

        @ApiModelProperty(value = "Descrição do perfil", example = "Cliente")
        String descricao
) {
}
