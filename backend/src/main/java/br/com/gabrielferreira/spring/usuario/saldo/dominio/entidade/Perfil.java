package br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PERFIL", schema = "dbo")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Generated
public class Perfil implements Serializable, GrantedAuthority {

    @Serial
    private static final long serialVersionUID = 8456353225172590010L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "NOME", nullable = false)
    private String nome;

    @Column(name = "DESCRICAO", nullable = false)
    private String descricao;

    @Override
    public String getAuthority() {
        return nome;
    }
}
