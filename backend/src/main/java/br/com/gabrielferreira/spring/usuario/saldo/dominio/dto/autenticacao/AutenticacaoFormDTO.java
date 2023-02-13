package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.autenticacao;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutenticacaoFormDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -2132474368541690403L;

    @ApiModelProperty(value = "E-mail do usuário", example = "test@email.com.br")
    @NotBlank(message = "E-mail não pode ser vazio.")
    @Email(message = "E-mail inválido.")
    private String email;

    @ApiModelProperty(value = "Senha do usuário", example = "123")
    @NotBlank(message = "Senha não pode ser vazio.")
    private String senha;
}
