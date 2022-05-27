package br.com.gabrielferreira.spring.usuario.saldo.entidade;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "usuario")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Saldo implements Serializable {

    private static final long serialVersionUID = -9124903408974073315L;

    @EqualsAndHashCode.Include
    private Long id;

    private BigDecimal deposito;
    private LocalDateTime dataDeposito;
    private Usuario usuario;

}
