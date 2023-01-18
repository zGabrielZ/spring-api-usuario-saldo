package br.com.gabrielferreira.spring.usuario.saldo.utils;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Perfil;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.enums.RoleEnum;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.enums.RoleEnum.*;

@Component
public class LoginUsuarioUtils {

    private LoginUsuarioUtils(){}

    public static Usuario getRecuperarUsuarioLogado(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() != null && authentication.getPrincipal() instanceof Usuario usuario){
            return usuario;
        }
        return null;
    }

    public static boolean isAdmin(){
        return getRoleEnum().equals(ROLE_ADMIN);
    }

    public static boolean isFuncionario(){
        return getRoleEnum().equals(ROLE_FUNCIONARIO);
    }

    public static boolean isCliente(){
        return getRoleEnum().equals(ROLE_CLIENTE);
    }

    private static RoleEnum getRoleEnum(){
        Usuario usuarioLogado = getRecuperarUsuarioLogado();
        if(usuarioLogado != null && usuarioLogado.getPerfis() != null){
            for (Perfil perfil : usuarioLogado.getPerfis()) {
                if(perfil.getNome().equals(ROLE_ADMIN.getRoleCompleta())){
                    return ROLE_ADMIN;
                } else if(perfil.getNome().equals(ROLE_FUNCIONARIO.getRoleCompleta())){
                    return ROLE_FUNCIONARIO;
                } else if(perfil.getNome().equals(ROLE_CLIENTE.getRoleCompleta())){
                    return ROLE_CLIENTE;
                }
            }
        }
        throw new ExcecaoPersonalizada("Nenhum perfil encaixado ao usuario");
    }

}
