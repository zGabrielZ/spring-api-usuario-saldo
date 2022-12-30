package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.pefil.PerfilViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.utils.MascarasUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class UsuarioDTOFactory implements Serializable {

    @Serial
    private static final long serialVersionUID = -4687700744639015221L;

    private UsuarioDTOFactory(){}

    public static UsuarioViewDTO toUsuarioViewDTO(Usuario usuario){
        List<PerfilViewDTO> perfis = PerfilDTOFactory.toPerfis(usuario.getPerfis());
        return new UsuarioViewDTO(usuario.getId(),usuario.getNome(),usuario.getEmail(), MascarasUtils.toCpfFormatado(usuario.getCpf()),usuario.getDataNascimento(),perfis);
    }

}
