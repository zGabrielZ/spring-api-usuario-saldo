package br.com.gabrielferreira.spring.usuario.saldo.entidade.dto;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.Saldo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SaldoFormDTO implements Serializable {

    private static final long serialVersionUID = -2398120816295097002L;

    @NotNull(message = "Déposito não pode ser vazio.")
    private BigDecimal deposito;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @NotNull(message = "Data do déposito não pode ser vazio.")
    private LocalDateTime dataDeposito;

    @NotNull(message = "Usuário não pode ser vazio.")
    private Long idUsuario;

    public SaldoFormDTO(Saldo saldo){
        this.deposito = saldo.getDeposito();
        this.dataDeposito = saldo.getDataDeposito();
        this.idUsuario = saldo.getUsuario().getId();
    }

}
