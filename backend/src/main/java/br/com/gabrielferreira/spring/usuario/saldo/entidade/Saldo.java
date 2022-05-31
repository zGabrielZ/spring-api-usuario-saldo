package br.com.gabrielferreira.spring.usuario.saldo.entidade;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "SALDO")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "usuario")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Saldo implements Serializable {

    private static final long serialVersionUID = -9124903408974073315L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "deposito",nullable = false)
    private BigDecimal deposito;

    @Column(name = "data_deposito", nullable = false)
    private LocalDateTime dataDeposito;

    @JoinColumn(name = "usuario_id",foreignKey = @ForeignKey(name="usuario_fk"))
    @ManyToOne
    private Usuario usuario;

}
