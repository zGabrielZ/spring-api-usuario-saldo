package br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade;

import lombok.*;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SALDO", schema = "dbo")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Saldo implements Serializable {

    @Serial
    private static final long serialVersionUID = -9124903408974073315L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "DEPOSITO", nullable = false)
    private BigDecimal deposito;

    @Column(name = "DATA_DEPOSITO", nullable = false)
    private LocalDateTime dataDeposito;

    @JoinColumn(name = "USUARIO_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;

    @JoinColumn(name = "USUARIO_DEPOSITANTE_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuarioDepositante;

    @OneToMany(mappedBy = "saldo", fetch = FetchType.LAZY)
    private List<UsuarioMovimentacao> usuariosMovimentacoes = new ArrayList<>();

}
