package br.com.gabrielferreira.spring.usuario.saldo.entidade.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SacarFormDTO implements Serializable {

    private static final long serialVersionUID = -7136821550686871414L;

    @NotNull(message = "É necessário informar a quantidade do saque.")
    private BigDecimal quantidade;

    @NotNull(message = "É necessário informar o usuário.")
    private Long idUsuario;
}
