package br.com.gabrielferreira.spring.usuario.saldo.entidade.dto;

import br.com.gabrielferreira.spring.usuario.saldo.entidade.Saque;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class SaqueViewDTO implements Serializable {

    private static final long serialVersionUID = -7136821550686871414L;


    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataSaque;

    private BigDecimal valor;

    public SaqueViewDTO(Saque saque){
        this.dataSaque = saque.getDataSaque();
        this.valor = saque.getValor();
    }

    public static Page<SaqueViewDTO> converterParaDto(Page<Saque> saques){
        return saques.map(SaqueViewDTO::new);
    }

}
