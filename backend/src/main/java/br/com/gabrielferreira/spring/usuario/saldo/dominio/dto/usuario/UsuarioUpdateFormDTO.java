package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.pefil.PerfilInsertFormDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioUpdateFormDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -2398120816295097002L;

    @ApiModelProperty(value = "Nome do usuário", example = "José Fernandes")
    @NotBlank(message = "Nome não pode ser vazio.")
    @Size(min = 5,max = 150,message = "O campo nome deve ter no mínimo 5 até 150 caracteres.")
    private String nome;

    @ApiModelProperty(value = "Data nascimento do usuário", example = "31/12/1990")
    @PastOrPresent(message = "Data nascimento não pode ser futura.")
    @NotNull(message = "Data nascimento não pode ser vazio.")
    private LocalDate dataNascimento;

    @ApiModelProperty(value = "Lista de perfis")
    private List<PerfilInsertFormDTO> perfis = new ArrayList<>();

}
