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
        return Optional.ofNullable((Usuario) authentication.getPrincipal());
    }

    public Map<String, Boolean> isPerfilUsuarioLogado(){
        Map<String, Boolean> map = new HashMap<>();
        recuperarUsuarioLogado().ifPresent(usuario -> {
            map.put(ROLE_ADMIN.getRole(), usuario.getPerfis().stream().anyMatch(perfil -> perfil.getNome().equals(ROLE_ADMIN.getRole())));
            map.put(ROLE_FUNCIONARIO.getRole(), usuario.getPerfis().stream().anyMatch(perfil -> perfil.getNome().equals(ROLE_FUNCIONARIO.getRole())));
            map.put(ROLE_CLIENTE.getRole(), usuario.getPerfis().stream().anyMatch(perfil -> perfil.getNome().equals(ROLE_CLIENTE.getRole())));
        });
        return map;
    }
}
