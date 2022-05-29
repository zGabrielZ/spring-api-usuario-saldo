package br.com.gabrielferreira.spring.usuario.saldo.entidade.dto;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.Usuario;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UsuarioFormDTO implements Serializable {

    private static final long serialVersionUID = -2398120816295097002L;

    private String nome;
    private String email;
    private String senha;
    private String cpf;

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
