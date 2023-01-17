package br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.factory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.pefil.PerfilInsertFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioInsertFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioUpdateFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Perfil;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class UsuarioEntidadeFactory implements Serializable {

    @Serial
    private static final long serialVersionUID = 2762513171034400055L;

    private UsuarioEntidadeFactory(){}

    public static Usuario toUsuarioInsertEntidade(UsuarioInsertFormDTO usuarioInsertFormDTO, List<PerfilInsertFormDTO> perfisDtos, BigDecimal valor, Usuario usuarioLogado){
        List<Perfil> perfis = PerfilEntidadeFactory.toPerfis(perfisDtos);
        return Usuario.builder()
                .nome(usuarioInsertFormDTO.getNome().trim())
                .email(usuarioInsertFormDTO.getEmail())
                .senha(usuarioInsertFormDTO.getSenha().trim())
                .cpf(usuarioInsertFormDTO.getCpf())
                .dataNascimento(usuarioInsertFormDTO.getDataNascimento())
                .saldoTotal(valor)
                .usuarioInclusao(usuarioLogado)
                .perfis(perfis)
                .build();
    }

    public static Usuario toUsuarioUpdateEntidade(UsuarioUpdateFormDTO usuarioUpdateFormDTO, Usuario usuario, List<PerfilInsertFormDTO> perfisDtos){
        List<Perfil> perfis = PerfilEntidadeFactory.toPerfis(perfisDtos);

        if(!perfis.isEmpty()){
            usuario.setPerfis(perfis);
        }

        usuario.setNome(usuarioUpdateFormDTO.getNome().trim());
        usuario.setDataNascimento(usuarioUpdateFormDTO.getDataNascimento());
        return usuario;
    }
}
