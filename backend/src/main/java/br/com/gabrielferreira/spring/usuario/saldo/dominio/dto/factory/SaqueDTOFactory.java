package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SacarViewDTO;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

public class SaqueDTOFactory implements Serializable {

    @Serial
    private static final long serialVersionUID = -4687700744639015221L;

    private SaqueDTOFactory(){}

    public static SacarViewDTO toSacarViewDTO(BigDecimal saldoTotal){
        return SacarViewDTO.builder().saldoTotal(saldoTotal).build();
    }
}
