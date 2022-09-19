package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SacarViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SaqueViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saque;
import org.springframework.data.domain.Page;

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

    public static SaqueViewDTO toSaqueViewDTO(Saque saque){
        return new SaqueViewDTO(saque.getId(),saque.getDataSaque(),saque.getValor());
    }

    public static Page<SaqueViewDTO> toPageSaldoViewDTO(Page<Saque> saques){
        return saques.map(SaqueDTOFactory::toSaqueViewDTO);
    }
}
