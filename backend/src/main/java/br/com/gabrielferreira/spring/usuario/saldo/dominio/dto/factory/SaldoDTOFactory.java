package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saldo;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class SaldoDTOFactory implements Serializable {

    private static final long serialVersionUID = -4687700744639015221L;

    public SaldoViewDTO toSaldoViewDTO(Saldo saldo){
        SaldoViewDTO saldoViewDTO = new SaldoViewDTO();
        saldoViewDTO.setId(saldo.getId());
        saldoViewDTO.setDeposito(saldo.getDeposito());
        saldoViewDTO.setDataDeposito(saldo.getDataDeposito());
        return saldoViewDTO;
    }
}
