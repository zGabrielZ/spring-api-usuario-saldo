package br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SAQUE")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Saque implements Serializable {

    private static final long serialVersionUID = -9124903408974073315L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "valor",nullable = false)
    private BigDecimal valor;

    @Column(name = "data_saque", nullable = false)
    private LocalDateTime dataSaque;

    @JoinColumn(name = "usuario_id",foreignKey = @ForeignKey(name="usuario_saque_fk"))
    @ManyToOne
    private Usuario usuario;

}
