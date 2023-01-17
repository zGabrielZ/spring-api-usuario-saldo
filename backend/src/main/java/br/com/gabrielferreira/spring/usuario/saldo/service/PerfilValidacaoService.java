package br.com.gabrielferreira.spring.usuario.saldo.service;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.pefil.PerfilInsertFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.enums.RoleEnum;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.enums.RoleEnum.*;
import static br.com.gabrielferreira.spring.usuario.saldo.utils.ValidacaoEnum.*;

@Service
@RequiredArgsConstructor
public class PerfilValidacaoService {

    private final PerfilService perfilService;

    public List<PerfilInsertFormDTO> validarPerfilUsuarioInsert(Usuario usuarioLogado, List<PerfilInsertFormDTO> perfis){
        boolean isAdmin = perfilService.isPerfilUsuarioLogado().get(ROLE_ADMIN.getRole());

        if(usuarioLogado != null && isAdmin && perfis.isEmpty()){
            throw new ExcecaoPersonalizada(PERFIL_USUARIO.getMensagem());
        } else if(usuarioLogado != null && !isAdmin){
            throw new ExcecaoPersonalizada(PERFIL_USUARIO_ADMIN.getMensagem());
        }

        return verificarPerfisDuplicados(perfis);
    }

    public List<PerfilInsertFormDTO> validarPerfilInformadoUsuarioInsert(List<PerfilInsertFormDTO> perfis){
        boolean isAdmin = perfilService.isPerfilUsuarioLogado().get(ROLE_ADMIN.getRole());
        if(isAdmin){
            return perfis;
        } else {
            return List.of(PerfilInsertFormDTO.builder().id(RoleEnum.ROLE_CLIENTE.getId()).build());
        }
    }

    private List<PerfilInsertFormDTO> verificarPerfisDuplicados(List<PerfilInsertFormDTO> perfis){
        perfis.forEach(perfilInsertFormDTO -> {
            int duplicados = Collections.frequency(perfis, perfilInsertFormDTO);

            if (duplicados > 1) {
                throw new ExcecaoPersonalizada(PERFIL_USUARIO_ADMIN_REPETIDO.getMensagem());
            }
        });

        return perfis;
    }
}
