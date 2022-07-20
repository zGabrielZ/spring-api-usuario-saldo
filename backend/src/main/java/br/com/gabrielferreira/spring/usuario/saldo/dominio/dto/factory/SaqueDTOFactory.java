package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SacarViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SaqueViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saque;
import org.springframework.data.domain.Page;

import java.io.Serializable;

public class SaqueDTOFactory implements Serializable {

    private static final long serialVersionUID = -4687700744639015221L;

    private SaqueDTOFactory(){}

    public static SacarViewDTO toSacarViewDTO(Saque saque){
        return new SacarViewDTO(saque.getValor());
    }

    public static SaqueViewDTO toSaqueViewDTO(Saque saque){
        return new SaqueViewDTO(saque.getDataSaque(), saque.getValor());
    }

    public static Page<SaqueViewDTO> toPageSaldoViewDTO(Page<Saque> saques){
        return saques.map(SaqueDTOFactory::toSaqueViewDTO);
    }
}
