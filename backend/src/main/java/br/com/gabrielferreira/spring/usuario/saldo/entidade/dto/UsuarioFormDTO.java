package br.com.gabrielferreira.spring.usuario.saldo.entidade.dto;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.Usuario;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UsuarioFormDTO implements Serializable {

    private static final long serialVersionUID = -2398120816295097002L;

    @NotBlank(message = "Nome não pode ser vazio.")
    @Size(min = 5,max = 150,message = "O campo nome deve ter no mínimo 5 até 150 caracteres.")
    private String nome;

    @NotBlank(message = "E-mail não pode ser vazio.")
    @Email(message = "E-mail inválido.")
    private String email;
    @NotBlank(message = "Senha não pode ser vazio.")
    private String senha;
    @NotBlank(message = "CPF não pode ser vazio.")
    @CPF(message = "CPF inválido.")
    private String cpf;

    @PastOrPresent(message = "Data nascimento não pode ser futura.")
    @NotNull(message = "Data nascimento não pode ser vazio.")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;

    public UsuarioFormDTO(Usuario usuario){
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.senha = usuario.getSenha();
        this.cpf = usuario.getCpf();
        this.dataNascimento = usuario.getDataNascimento();
    }

}
