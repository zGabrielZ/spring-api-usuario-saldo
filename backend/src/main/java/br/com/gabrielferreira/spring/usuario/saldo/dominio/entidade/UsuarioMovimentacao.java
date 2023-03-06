package br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade;

import lombok.*;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static br.com.gabrielferreira.spring.usuario.saldo.utils.ConstantesUtils.AMERICA_SAO_PAULO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USUARIO_MOVIMENTACAO", schema = "dbo")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UsuarioMovimentacao implements Serializable {

    @Serial
    private static final long serialVersionUID = -9124903408974073315L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @JoinColumn(name = "USUARIO_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;

    @Column(name = "SALDO_ATUAL", nullable = false)
    private BigDecimal saldoAtual;

    @Column(name = "DATA_CADASTRO", nullable = false)
    private LocalDateTime dataCadastro;

    @Column(name = "QUANTIDADE_INFORMADA", nullable = false)
    private BigDecimal quantidadeInformada;

    @Column(name = "DESCRICAO", nullable = false)
    private String descricao;

    @JoinColumn(name = "SITUACAO_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Situacao situacao;

    @JoinColumn(name = "SALDO_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Saldo saldo;

    @JoinColumn(name = "SAQUE_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Saque saque;

    @PrePersist
    private void preInsercao(){
        dataCadastro = LocalDateTime.now(ZoneId.of(AMERICA_SAO_PAULO));
    }

}
