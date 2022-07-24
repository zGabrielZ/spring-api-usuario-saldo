package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoTotalViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saldo;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.math.BigDecimal;

public class SaldoDTOFactory implements Serializable {

    private static final long serialVersionUID = -4687700744639015221L;

    private SaldoDTOFactory(){}

    public static SaldoViewDTO toSaldoViewDTO(Saldo saldo){
        return SaldoViewDTO.builder().id(saldo.getId()).deposito(saldo.getDeposito())
                        .dataDeposito(saldo.getDataDeposito()).build();
    }

    public static Page<SaldoViewDTO> toPageSaldoViewDTO(Page<Saldo> saldos){
        return saldos.map(SaldoDTOFactory::toSaldoViewDTO);
    }

    public static SaldoTotalViewDTO toSaldoTotalViewDTO(BigDecimal saldoTotal){
        return SaldoTotalViewDTO.builder().saldoTotal(saldoTotal).build();
    }
}
