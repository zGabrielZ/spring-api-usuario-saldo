package br.com.gabrielferreira.spring.usuario.saldo.entidade.dto;

import br.com.gabrielferreira.spring.usuario.saldo.entidade.Saldo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SaldoViewDTO implements Serializable {

    private static final long serialVersionUID = -2398120816295097002L;

    private Long id;
    private BigDecimal deposito;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataDeposito;


    public SaldoViewDTO(Saldo saldo){
        this.id = saldo.getId();
        this.deposito = saldo.getDeposito();
        this.dataDeposito = saldo.getDataDeposito();
    }

    public static List<SaldoViewDTO> listParaDto(List<Saldo> saldos){
        return saldos.stream().map(SaldoViewDTO::new).collect(Collectors.toList());
    }

}
