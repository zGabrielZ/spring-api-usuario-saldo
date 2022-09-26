package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.autenticacao;

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

    @NotBlank(message = "E-mail não pode ser vazio.")
    @Email(message = "E-mail inválido.")
    private String email;

    @NotBlank(message = "Senha não pode ser vazio.")
    private String senha;
}
