package br.com.gabrielferreira.spring.usuario.saldo.entidade;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
@Entity
@Table(name = "SAQUE")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "usuario")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Saque implements Serializable {

    private static final long serialVersionUID = -9124903408974073315L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "valor",nullable = false)
    private BigDecimal valor;

    @JoinColumn(name = "usuario_id",foreignKey = @ForeignKey(name="usuario_saque_fk"))
    @ManyToOne
    private Usuario usuario;

}
