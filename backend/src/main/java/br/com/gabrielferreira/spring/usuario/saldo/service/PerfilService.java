package br.com.gabrielferreira.spring.usuario.saldo.service;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.enums.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PerfilService {

    public Usuario recuperarUsuarioLogado(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() != null && !authentication.getPrincipal().equals("anonymousUser")){
            return (Usuario) authentication.getPrincipal();
        }
        return null;
    }

    public boolean isContemPerfilAdminUsuarioLogado(){
        Usuario usuario = recuperarUsuarioLogado();
        if(usuario != null){
            return usuario.getPerfis().stream().anyMatch(f -> f.getId().equals(RoleEnum.ROLE_ADMIN.getId()));
        }
        return false;
    }
}
