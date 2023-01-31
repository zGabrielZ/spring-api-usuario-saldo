package br.com.gabrielferreira.spring.usuario.saldo.service;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.pefil.PerfilInsertFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.enums.RoleEnum;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import org.springframework.stereotype.Service;
import java.util.*;
import static br.com.gabrielferreira.spring.usuario.saldo.utils.ValidacaoEnum.*;

@Service
public class PerfilValidacaoService {

    public List<PerfilInsertFormDTO> validarPerfilUsuarioInsert(List<PerfilInsertFormDTO> perfis, Usuario usuarioLogado, boolean isAdmin, boolean isFuncionario, boolean isCliente){
        if(usuarioLogado != null){
            if(isAdmin && perfis.isEmpty()){
                throw new ExcecaoPersonalizada(PERFIL_USUARIO.getMensagem());
            }

            boolean isNaoAdminLogado = isNaoAdmin(isAdmin, isFuncionario, isCliente);

            if(isNaoAdminLogado && !perfis.isEmpty()){
                throw new ExcecaoPersonalizada(PERFIL_USUARIO_ADMIN.getMensagem());
            }
        }
        return verificarPerfisDuplicados(perfis);
    }

    public void validarPerfilUsuarioUpdate(List<PerfilInsertFormDTO> perfis, Usuario usuarioLogado, Long idUsuarioEncontrado, boolean isAdmin){
        if(usuarioLogado != null){
            if(!isAdmin && !usuarioLogado.getId().equals(idUsuarioEncontrado)){
                throw new ExcecaoPersonalizada(USUARIO_ATUALIZAR_PERMISSAO.getMensagem());
            } else if (!isAdmin && !perfis.isEmpty()){
                throw new ExcecaoPersonalizada(USUARIO_INCLUIR_ALTERAR.getMensagem());
            } else if(isAdmin && perfis.isEmpty()){
                throw new ExcecaoPersonalizada(PERFIL_USUARIO.getMensagem());
            }
        }
    }

    public void validarPerfilUsuarioDelete(Usuario usuarioEncontrado, Usuario usuarioLogado){
        if (usuarioLogado != null && usuarioLogado.getId().equals(usuarioEncontrado.getId())) {
            throw new ExcecaoPersonalizada(PERFIL_USUARIO_DELETAR_ADMIN_PROPRIO.getMensagem());
        }
    }

    public List<PerfilInsertFormDTO> validarPerfilInformadoUsuarioInsert(List<PerfilInsertFormDTO> perfis, boolean isAdmin){
        if(isAdmin){
            return perfis;
        } else {
            return List.of(PerfilInsertFormDTO.builder().id(RoleEnum.ROLE_CLIENTE.getId()).build());
        }
    }

    public void validarPerfilUsuarioVisualizacao(Long idUsuarioEncontrado, Usuario usuarioLogado, boolean isAdmin, boolean isFuncionario, boolean isCliente){
        boolean isNaoAdminLogado = isNaoAdmin(isAdmin, isFuncionario, isCliente);
        if(usuarioLogado != null && !usuarioLogado.getId().equals(idUsuarioEncontrado) && isNaoAdminLogado){
            throw new ExcecaoPersonalizada(PERFIL_USUARIO_DADOS_ADMIN.getMensagem());
        }
    }

    public void validarPerfilBuscarSaldoPorUsuario(Usuario usuarioLogado, boolean isAdmin, boolean isFuncionario, Long idUsuarioEncontrado){
        if(!(isAdmin || isFuncionario) && !usuarioLogado.getId().equals(idUsuarioEncontrado)){
            throw new ExcecaoPersonalizada(LISTA_SALDOS.getMensagem());
        }
    }

    public void validarPerfilUsuarioSaldo(Usuario usuarioLogado, Long idUsuarioEncontrado){
        if (usuarioLogado != null && usuarioLogado.getId().equals(idUsuarioEncontrado)) {
            throw new ExcecaoPersonalizada(USUARIO_INCLUIR_DEPOSITO_ADMIN.getMensagem());
        }
    }

    public void validarPerfilUsuarioVisualizacaoSaldo(Usuario usuarioLogado, boolean isAdmin, Usuario usuarioEncontrado){
        if(!isAdmin && !usuarioLogado.getId().equals(usuarioEncontrado.getId())){
            throw new ExcecaoPersonalizada(VISUALIZAR_SALDO_TOTAL.getMensagem());
        }
    }

    public void verificarSituacaoUsuarioLogado(Usuario usuarioLogado){
        if(usuarioLogado != null && usuarioLogado.isExcluido()){
            throw new ExcecaoPersonalizada(OPERACAO_USUARIO_NAO_ENCONTRADO.getMensagem());
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

    private boolean isNaoAdmin(boolean isAdmin, boolean isFuncionario, boolean isCliente){
        return (isCliente || isFuncionario) && !isAdmin;
    }
}
