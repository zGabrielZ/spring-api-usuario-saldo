package br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USUARIO")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario implements Serializable {

    private static final long serialVersionUID = -7061711027863603940L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "nome",nullable = false)
    private String nome;

    @Column(name = "email",nullable = false)
    private String email;

    @Column(name = "senha",nullable = false)
    private String senha;

    @Column(name = "cpf",nullable = false)
    private String cpf;

    @Column(name = "data_nascimento",nullable = false)
    private LocalDate dataNascimento;

    @Builder.Default
    @OneToMany(cascade = CascadeType.REMOVE,mappedBy = "usuario")
    private List<Saldo> saldos = new ArrayList<>();

    @Column(name = "saldo_total")
    private BigDecimal saldoTotal;

    @Builder.Default
    @OneToMany(cascade = CascadeType.REMOVE,mappedBy = "usuario")
    private List<Saque> saques = new ArrayList<>();

}
