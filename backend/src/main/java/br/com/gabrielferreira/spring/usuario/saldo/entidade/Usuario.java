package br.com.gabrielferreira.spring.usuario.saldo.entidade;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario implements Serializable {

    private static final long serialVersionUID = -7061711027863603940L;

    @EqualsAndHashCode.Include
    private Long id;

    private String nome;
    private String email;
    private String senha;
    private String cpf;
    private LocalDate dataNascimento;
    private List<Saldo> saldos = new ArrayList<>();
}
