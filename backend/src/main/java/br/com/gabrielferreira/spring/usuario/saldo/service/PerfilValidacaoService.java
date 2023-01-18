package br.com.gabrielferreira.spring.usuario.saldo.service;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.pefil.PerfilInsertFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.enums.RoleEnum;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import org.springframework.stereotype.Service;

import java.util.*;

import static br.com.gabrielferreira.spring.usuario.saldo.utils.LoginUsuarioUtils.*;
import static br.com.gabrielferreira.spring.usuario.saldo.utils.ValidacaoEnum.*;

@Service
public class PerfilValidacaoService {

    public List<PerfilInsertFormDTO> validarPerfilUsuarioInsert(List<PerfilInsertFormDTO> perfis){
        if(getRecuperarUsuarioLogado() != null && isAdmin() && perfis.isEmpty()){
            throw new ExcecaoPersonalizada(PERFIL_USUARIO.getMensagem());
        } else if(getRecuperarUsuarioLogado() != null && !isAdmin()){
            throw new ExcecaoPersonalizada(PERFIL_USUARIO_ADMIN.getMensagem());
        }

        return verificarPerfisDuplicados(perfis);
    }

    public List<PerfilInsertFormDTO> validarPerfilInformadoUsuarioInsert(List<PerfilInsertFormDTO> perfis){
        if(isAdmin()){
            return perfis;
        } else {
            return List.of(PerfilInsertFormDTO.builder().id(RoleEnum.ROLE_CLIENTE.getId()).build());
        }
    }

    public void validarPerfilUsuarioVisualizacao(Long idUsuarioEncontrado){
        if(getRecuperarUsuarioLogado() != null && !getRecuperarUsuarioLogado().getId().equals(idUsuarioEncontrado) && !isAdmin()){
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
