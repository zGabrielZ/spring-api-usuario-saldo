package br.com.gabrielferreira.spring.usuario.saldo.service;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.pefil.PerfilInsertFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.enums.RoleEnum;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import org.springframework.stereotype.Service;

import java.util.*;

import static br.com.gabrielferreira.spring.usuario.saldo.utils.LoginUsuarioUtils.*;
import static br.com.gabrielferreira.spring.usuario.saldo.utils.LoginUsuarioUtils.isAdmin;
import static br.com.gabrielferreira.spring.usuario.saldo.utils.ValidacaoEnum.*;

@Service
public class PerfilValidacaoService {

    public List<PerfilInsertFormDTO> validarPerfilUsuarioInsert(List<PerfilInsertFormDTO> perfis){
        Usuario usuarioLogado = getRecuperarUsuarioLogado();

        if(usuarioLogado != null){
            if(isAdmin() && perfis.isEmpty()){
                throw new ExcecaoPersonalizada(PERFIL_USUARIO.getMensagem());
            } else if(!isAdmin() && !perfis.isEmpty()){
                throw new ExcecaoPersonalizada(PERFIL_USUARIO_ADMIN.getMensagem());
            }
        }

        return verificarPerfisDuplicados(perfis);
    }

    public void validarPerfilUsuarioUpdate(List<PerfilInsertFormDTO> perfis, Usuario usuarioEncontrado){
        Usuario usuarioLogado = getRecuperarUsuarioLogado();
        if(usuarioLogado != null){
            if(!isAdmin() && !usuarioLogado.getId().equals(usuarioEncontrado.getId())){
                throw new ExcecaoPersonalizada(USUARIO_ATUALIZAR_PERMISSAO.getMensagem());
            } else if (!isAdmin() && usuarioLogado.getId().equals(usuarioEncontrado.getId()) && !perfis.isEmpty()){
                throw new ExcecaoPersonalizada(USUARIO_INCLUIR_ALTERAR.getMensagem());
            } else if(isAdmin() && perfis.isEmpty()){
                throw new ExcecaoPersonalizada(PERFIL_USUARIO.getMensagem());
            }
        }
    }

    public List<PerfilInsertFormDTO> validarPerfilInformadoUsuarioInsert(List<PerfilInsertFormDTO> perfis){
        if(isAdmin()){
            return perfis;
        } else {
            return List.of(PerfilInsertFormDTO.builder().id(RoleEnum.ROLE_CLIENTE.getId()).build());
        }
    }

    public void validarPerfilUsuarioVisualizacao(Long idUsuarioEncontrado){
        Usuario usuarioLogado = getRecuperarUsuarioLogado();
        if(usuarioLogado != null && !usuarioLogado.getId().equals(idUsuarioEncontrado) && !isAdmin()){
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
