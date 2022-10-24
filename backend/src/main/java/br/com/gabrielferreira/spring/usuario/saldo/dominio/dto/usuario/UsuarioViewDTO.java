package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.pefil.PerfilViewDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.List;

public record UsuarioViewDTO (

        @ApiModelProperty(value = "Código do usuário",example = "1")
        Long id,

        @ApiModelProperty(value = "Nome do usuário",example = "Gabriel Ferreira")
        String nome,

        @ApiModelProperty(value = "E-mail do usuário",example = "ferreiragabriel2612@gmail.com")
        String email,

        @ApiModelProperty(value = "CPF do usuário", example = "741.792.450-60")
        String cpf,

        @ApiModelProperty(value = "Data de nascimento do usuário", example = "26/12/1997")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataNascimento,

        @ApiModelProperty(value = "Lista de perfis")
        List<PerfilViewDTO> perfis
) {

}
