package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioInsertFormDTO implements Serializable {

    private static final long serialVersionUID = -2398120816295097002L;

    @ApiModelProperty(value = "Nome do usuário", example = "Gabriel Ferreira")
    @NotBlank(message = "Nome não pode ser vazio.")
    @Size(min = 5,max = 150,message = "O campo nome deve ter no mínimo 5 até 150 caracteres.")
    private String nome;

    @ApiModelProperty(value = "E-mail do usuário", example = "ferreiragabriel2612@gmail.com")
    @NotBlank(message = "E-mail não pode ser vazio.")
    @Email(message = "E-mail inválido.")
    private String email;

    @ApiModelProperty(value = "Senha do usuário", example = "123")
    @NotBlank(message = "Senha não pode ser vazio.")
    private String senha;

    @ApiModelProperty(value = "CPF do usuário", example = "84269756071")
    @NotBlank(message = "CPF não pode ser vazio.")
    @CPF(message = "CPF inválido.")
    private String cpf;

    @ApiModelProperty(value = "Data de nascimento do usuário", example = "26/12/1997")
    @PastOrPresent(message = "Data nascimento não pode ser futura.")
    @NotNull(message = "Data nascimento não pode ser vazio.")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;

}
