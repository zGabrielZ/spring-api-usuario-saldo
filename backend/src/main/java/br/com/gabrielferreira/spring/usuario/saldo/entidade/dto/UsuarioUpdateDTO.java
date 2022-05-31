package br.com.gabrielferreira.spring.usuario.saldo.entidade.dto;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.Usuario;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UsuarioUpdateDTO implements Serializable {

    private static final long serialVersionUID = -2398120816295097002L;

    @NotBlank(message = "Nome não pode ser vazio.")
    @Size(min = 5,max = 150,message = "O campo nome deve ter no mínimo 5 até 150 caracteres.")
    private String nome;

    @PastOrPresent(message = "Data nascimento não pode ser futura.")
    @NotNull(message = "Data nascimento não pode ser vazio.")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;

    public UsuarioUpdateDTO(Usuario usuario){
        this.nome = usuario.getNome();
        this.dataNascimento = usuario.getDataNascimento();
    }

}
