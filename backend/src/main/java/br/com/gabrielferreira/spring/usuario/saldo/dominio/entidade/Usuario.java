package br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static br.com.gabrielferreira.spring.usuario.saldo.utils.ConstantesUtils.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USUARIO", schema = "dbo")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Generated
public class Usuario implements Serializable, UserDetails {

    @Serial
    private static final long serialVersionUID = -7061711027863603940L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "NOME", nullable = false)
    private String nome;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "SENHA", nullable = false)
    private String senha;

    @Column(name = "CPF", nullable = false, unique = true)
    private String cpf;

    @Column(name = "DATA_NASCIMENTO", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "SALDO_TOTAL", nullable = false)
    private BigDecimal saldoTotal;

    @JoinColumn(name = "USUARIO_INCLUSAO_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuarioInclusao;

    @Column(name = "DATA_INCLUSAO", nullable = false)
    private ZonedDateTime dataInclusao;

    @JoinColumn(name = "USUARIO_ALTERACAO_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuarioAlteracao;

    @Column(name = "DATA_ALTERACAO")
    private ZonedDateTime dataAlteracao;

    @JoinColumn(name = "USUARIO_EXCLUSAO_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuarioExclusao;

    @Column(name = "DATA_EXCLUSAO")
    private ZonedDateTime dataExclusao;

    @Column(name = "FLAG_EXCLUIDO", nullable = false)
    private Boolean excluido;

    @Builder.Default
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<Saldo> saldos = new ArrayList<>();

    @Builder.Default
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<Saque> saques = new ArrayList<>();

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USUARIOS_PERFIL", schema = "dbo",
                joinColumns = @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID", table = "USUARIO"),
                inverseJoinColumns = @JoinColumn(name = "PERFIL_ID", referencedColumnName = "ID", table = "PERFIL"))
    private List<Perfil> perfis = new ArrayList<>();

    @PrePersist
    private void preInsercao(){
        dataInclusao = ZonedDateTime.now(ZoneId.of(AMERICA_SAO_PAULO));
    }

    @PreUpdate
    private void preUpdate(){
        dataAlteracao = ZonedDateTime.now(ZoneId.of(AMERICA_SAO_PAULO));
    }

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

    public boolean isExcluido(){
        return this.excluido;
    }
}
