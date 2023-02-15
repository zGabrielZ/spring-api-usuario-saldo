package br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleEnum {

    ROLE_ADMIN(1L, "Admin", "ADMIN", "ROLE_ADMIN"),
    ROLE_FUNCIONARIO(2L, "Funcionário", "FUNCIONARIO", "ROLE_FUNCIONARIO"),
    ROLE_CLIENTE(3L, "Cliente", "CLIENTE", "ROLE_CLIENTE");

    private final Long id;
    private final String descricao;

    private final String roleResumida;

    private final String roleCompleta;

}
