package br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USUARIO")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Generated
public class Usuario implements Serializable, UserDetails {

    @Serial
    private static final long serialVersionUID = -7061711027863603940L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "senha", nullable = false)
    private String senha;

    @Column(name = "cpf", nullable = false, unique = true)
    private String cpf;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Builder.Default
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<Saldo> saldos = new ArrayList<>();

    @Column(name = "saldo_total")
    private BigDecimal saldoTotal;

    @Builder.Default
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<Saque> saques = new ArrayList<>();

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USUARIOS_PERFIL",
                joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id", table = "USUARIO"),
                inverseJoinColumns = @JoinColumn(name = "perfil_id", referencedColumnName = "id", table = "PERFIL"))
    private List<Perfil> perfis = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return perfis;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
