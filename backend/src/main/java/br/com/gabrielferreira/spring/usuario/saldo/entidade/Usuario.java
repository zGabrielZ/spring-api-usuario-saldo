package br.com.gabrielferreira.spring.usuario.saldo.entidade;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "USUARIO")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
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

    @OneToMany(cascade = CascadeType.REMOVE,fetch = FetchType.EAGER,mappedBy = "usuario")
    private List<Saldo> saldos = new ArrayList<>();

    @Column(name = "saldo_total")
    private BigDecimal saldoTotal;

    public Usuario(Long id, String nome, String email, String senha, String cpf, LocalDate dataNascimento) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
    }

    public void adicionarSaldo(Saldo saldo){
        this.saldos.add(saldo);
    }
}
