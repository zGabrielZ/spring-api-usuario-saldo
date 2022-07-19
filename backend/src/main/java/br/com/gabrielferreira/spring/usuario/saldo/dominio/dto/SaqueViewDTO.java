package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saque;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class SaqueViewDTO implements Serializable {

    private static final long serialVersionUID = -7136821550686871414L;


    @ApiModelProperty(value = "Data do saque", example = "26/06/2022 12:00:00")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataSaque;

    @ApiModelProperty(value = "Valor saque do usuário", example = "500.00")
    private BigDecimal valor;

    public SaqueViewDTO(Saque saque){
        this.dataSaque = saque.getDataSaque();
        this.valor = saque.getValor();
    }

    public static Page<SaqueViewDTO> converterParaDto(Page<Saque> saques){
        return saques.map(SaqueViewDTO::new);
    }

}
