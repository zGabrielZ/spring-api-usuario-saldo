package br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleEnum {

    ROLE_ADMIN(1L, "Admin", "ADMIN"),
    ROLE_FUNCIONARIO(2L, "Funcionário", "FUNCIONARIO"),
    ROLE_CLIENTE(3L, "Cliente", "CLIENTE");

    private final Long id;
    private final String descricao;

    private final String role;

    public static String getDescricao(Long id){
        for (RoleEnum role : RoleEnum.values()) {
            if(role.getId().equals(id)){
                return role.getDescricao();
            }
        }
        return null;
    }

}
