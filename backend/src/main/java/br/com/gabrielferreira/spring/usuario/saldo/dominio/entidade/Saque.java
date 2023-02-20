package br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade;

import lombok.*;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SAQUE", schema = "dbo")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Saque implements Serializable {

    @Serial
    private static final long serialVersionUID = -9124903408974073315L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "VALOR", nullable = false)
    private BigDecimal valor;

    @Column(name = "DATA_SAQUE", nullable = false)
    private LocalDateTime dataSaque;

    @JoinColumn(name = "USUARIO_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;

}
