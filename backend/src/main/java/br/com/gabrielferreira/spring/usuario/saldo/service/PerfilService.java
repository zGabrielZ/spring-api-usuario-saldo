package br.com.gabrielferreira.spring.usuario.saldo.service;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.enums.RoleEnum.*;

@Service
@RequiredArgsConstructor
public class PerfilService {

    public Optional<Usuario> recuperarUsuarioLogado(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() != null && authentication.getPrincipal().equals("anonymousUser")){
            return Optional.empty();
        }
        return Optional.ofNullable((Usuario) authentication.getPrincipal());
    }

    public Map<String, Boolean> isPerfilUsuarioLogado(){
        Map<String, Boolean> map = new HashMap<>();
        recuperarUsuarioLogado().ifPresent(usuario -> {
            map.put(ROLE_ADMIN.getRoleCompleta(), usuario.getPerfis().stream().anyMatch(perfil -> perfil.getNome().equals(ROLE_ADMIN.getRoleCompleta())));
            map.put(ROLE_FUNCIONARIO.getRoleCompleta(), usuario.getPerfis().stream().anyMatch(perfil -> perfil.getNome().equals(ROLE_FUNCIONARIO.getRoleCompleta())));
            map.put(ROLE_CLIENTE.getRoleResumida(), usuario.getPerfis().stream().anyMatch(perfil -> perfil.getNome().equals(ROLE_CLIENTE.getRoleCompleta())));
        });
        return map;
    }
}
