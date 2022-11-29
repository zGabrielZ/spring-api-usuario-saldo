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
@Table(name = "PERFIL")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Generated
public class Perfil implements Serializable, GrantedAuthority {

    @Serial
    private static final long serialVersionUID = 8456353225172590010L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Override
    public String getAuthority() {
        return nome;
    }
}
