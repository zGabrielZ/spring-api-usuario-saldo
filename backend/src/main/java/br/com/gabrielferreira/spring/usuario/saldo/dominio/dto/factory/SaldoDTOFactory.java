package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saldo;
import org.springframework.data.domain.Page;

import java.io.Serializable;

public class SaldoDTOFactory implements Serializable {

    private static final long serialVersionUID = -4687700744639015221L;

    private SaldoDTOFactory(){}

    public static SaldoViewDTO toSaldoViewDTO(Saldo saldo){
        SaldoViewDTO saldoViewDTO = new SaldoViewDTO();
        saldoViewDTO.setId(saldo.getId());
        saldoViewDTO.setDeposito(saldo.getDeposito());
        saldoViewDTO.setDataDeposito(saldo.getDataDeposito());
        return saldoViewDTO;
    }

    public static Page<SaldoViewDTO> toPageSaldoViewDTO(Page<Saldo> saldos){
        return saldos.map(SaldoDTOFactory::toSaldoViewDTO);
    }
}
