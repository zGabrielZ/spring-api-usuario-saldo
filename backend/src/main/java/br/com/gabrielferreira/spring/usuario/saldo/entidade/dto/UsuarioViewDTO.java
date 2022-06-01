package br.com.gabrielferreira.spring.usuario.saldo.entidade.dto;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.Usuario;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UsuarioViewDTO implements Serializable {

    private static final long serialVersionUID = -2398120816295097002L;

    @ApiModelProperty(value = "Código do usuário",example = "1")
    private Long id;
    @ApiModelProperty(value = "Nome do usuário",example = "Gabriel Ferreira")
    private String nome;
    @ApiModelProperty(value = "E-mail do usuário",example = "ferreiragabriel2612@gmail.com")
    private String email;
    @ApiModelProperty(value = "CPF do usuário", example = "84269756071")
    private String cpf;

    @ApiModelProperty(value = "Data de nascimento do usuário", example = "26/12/1997")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;

    public UsuarioViewDTO(Usuario usuario){
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.cpf = usuario.getCpf();
        this.dataNascimento = usuario.getDataNascimento();
    }

    public static Page<UsuarioViewDTO> converterParaDto(Page<Usuario> usuarios){
        return  usuarios.map(UsuarioViewDTO::new);
    }

}
