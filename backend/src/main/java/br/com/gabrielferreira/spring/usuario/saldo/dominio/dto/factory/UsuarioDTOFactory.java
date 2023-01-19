package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.pefil.PerfilInsertFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioInsertResponseDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioUpdateResponseDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.utils.MascarasUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class UsuarioDTOFactory implements Serializable {

    @Serial
    private static final long serialVersionUID = -4687700744639015221L;

    private UsuarioDTOFactory(){}

    public static UsuarioInsertResponseDTO toUsuarioInsertResponseDTO(Usuario usuario, String descricao){
        List<PerfilInsertFormDTO> perfis = PerfilDTOFactory.toPerfisInsert(usuario.getPerfis());
        UsuarioInsertResponseDTO usuarioInsertResponseDTO = new UsuarioInsertResponseDTO();
        usuarioInsertResponseDTO.setId(usuario.getId());
        usuarioInsertResponseDTO.setNome(usuario.getNome());
        usuarioInsertResponseDTO.setEmail(usuario.getEmail());
        usuarioInsertResponseDTO.setSenha(descricao);
        usuarioInsertResponseDTO.setDataNascimento(usuario.getDataNascimento());
        usuarioInsertResponseDTO.setPerfis(perfis);
        usuarioInsertResponseDTO.setCpf(MascarasUtils.toCpfFormatado(usuario.getCpf()));
        return usuarioInsertResponseDTO;
    }

    public static UsuarioUpdateResponseDTO toUsuarioUpdateResponseDTO(Usuario usuario){
        List<PerfilInsertFormDTO> perfis = PerfilDTOFactory.toPerfisInsert(usuario.getPerfis());
        UsuarioUpdateResponseDTO usuarioUpdateResponseDTO = new UsuarioUpdateResponseDTO();
        usuarioUpdateResponseDTO.setId(usuario.getId());
        usuarioUpdateResponseDTO.setNome(usuario.getNome());
        usuarioUpdateResponseDTO.setDataNascimento(usuario.getDataNascimento());
        usuarioUpdateResponseDTO.setPerfis(perfis);
        return usuarioUpdateResponseDTO;
    }

}
