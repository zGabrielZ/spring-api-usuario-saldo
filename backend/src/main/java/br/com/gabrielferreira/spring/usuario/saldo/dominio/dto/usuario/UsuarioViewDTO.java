package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@lombok.Generated
public class UsuarioViewDTO implements Serializable {

    private static final long serialVersionUID = -2398120816295097002L;

    @ApiModelProperty(value = "Código do usuário",example = "1")
    private Long id;

    @ApiModelProperty(value = "Nome do usuário",example = "Gabriel Ferreira")
    private String nome;

    @ApiModelProperty(value = "E-mail do usuário",example = "ferreiragabriel2612@gmail.com")
    private String email;

    @ApiModelProperty(value = "CPF do usuário", example = "741.792.450-60")
    private String cpf;

    @ApiModelProperty(value = "Data de nascimento do usuário", example = "26/12/1997")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;

}
