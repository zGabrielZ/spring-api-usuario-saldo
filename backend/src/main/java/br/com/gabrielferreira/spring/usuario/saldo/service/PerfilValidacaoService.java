package br.com.gabrielferreira.spring.usuario.saldo.service;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.pefil.PerfilInsertFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.enums.RoleEnum;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.*;

import static br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.enums.RoleEnum.*;
import static br.com.gabrielferreira.spring.usuario.saldo.utils.ValidacaoEnum.*;

@Service
@RequiredArgsConstructor
public class PerfilValidacaoService {

    private final PerfilService perfilService;

    @Setter
    @Getter
    private boolean adminUsuarioLogado;

    public List<PerfilInsertFormDTO> validarPerfilUsuarioInsert(Usuario usuarioLogado, List<PerfilInsertFormDTO> perfis){
        boolean isAdmin = perfilService.isPerfilUsuarioLogado().containsKey(ROLE_ADMIN.getRoleCompleta());
        setAdminUsuarioLogado(isAdmin);

        if(usuarioLogado != null && isAdminUsuarioLogado() && perfis.isEmpty()){
            throw new ExcecaoPersonalizada(PERFIL_USUARIO.getMensagem());
        } else if(usuarioLogado != null && !isAdminUsuarioLogado()){
            throw new ExcecaoPersonalizada(PERFIL_USUARIO_ADMIN.getMensagem());
        }

        return verificarPerfisDuplicados(perfis);
    }

    public List<PerfilInsertFormDTO> validarPerfilInformadoUsuarioInsert(List<PerfilInsertFormDTO> perfis){
        if(isAdminUsuarioLogado()){
            return perfis;
        } else {
            return List.of(PerfilInsertFormDTO.builder().id(RoleEnum.ROLE_CLIENTE.getId()).build());
        }
    }

    public void validarPerfilUsuarioVisualizacao(Long idUsuarioEncontrado, Usuario usuarioLogado){
        boolean isAdmin = perfilService.isPerfilUsuarioLogado().get(ROLE_ADMIN.getRoleCompleta());
        setAdminUsuarioLogado(isAdmin);

        if(usuarioLogado != null && !usuarioLogado.getId().equals(idUsuarioEncontrado) && !isAdminUsuarioLogado()){
            throw new ExcecaoPersonalizada(PERFIL_USUARIO_DADOS_ADMIN.getMensagem());
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
