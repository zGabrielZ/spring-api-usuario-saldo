package br.com.gabrielferreira.spring.usuario.saldo.utils;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Perfil;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.enums.RoleEnum;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
        return getRolesEnuns().contains(ROLE_ADMIN);
    }

    public static boolean isFuncionario(){
        return getRolesEnuns().contains(ROLE_FUNCIONARIO);
    }

    public static boolean isCliente(){
        return getRolesEnuns().contains(ROLE_CLIENTE);
    }

    private static List<RoleEnum> getRolesEnuns(){
        Usuario usuarioLogado = getRecuperarUsuarioLogado();
        List<RoleEnum> roles = new ArrayList<>();
        if(usuarioLogado != null && usuarioLogado.getPerfis() != null){
            for (Perfil perfil : usuarioLogado.getPerfis()) {
                if(perfil.getNome().equals(ROLE_ADMIN.getRoleCompleta())){
                    roles.add(ROLE_ADMIN);
                } else if(perfil.getNome().equals(ROLE_FUNCIONARIO.getRoleCompleta())){
                    roles.add(ROLE_FUNCIONARIO);
                } else if(perfil.getNome().equals(ROLE_CLIENTE.getRoleCompleta())){
                    roles.add(ROLE_CLIENTE);
                }
            }
        }
        return roles;
    }

}
