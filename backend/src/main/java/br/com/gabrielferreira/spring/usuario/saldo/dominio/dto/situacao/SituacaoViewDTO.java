package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.situacao;

import java.io.Serializable;

public record SituacaoViewDTO(

        Long id,

        String descricao,

        String codigo
) implements Serializable {
}
