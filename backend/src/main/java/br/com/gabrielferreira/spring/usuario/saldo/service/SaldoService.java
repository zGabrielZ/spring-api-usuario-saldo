package br.com.gabrielferreira.spring.usuario.saldo.service;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory.SaldoDTOFactory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saldo;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saque;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.factory.SaldoEntidadeFactory;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.SaldoRepositorio;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.SaqueRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import static br.com.gabrielferreira.spring.usuario.saldo.utils.ValidacaoEnum.DEPOSITO_MENOR_IGUAL_ZERO;

@Service
@RequiredArgsConstructor
public class SaldoService {

    private final UsuarioService usuarioService;

    private final SaldoRepositorio saldoRepositorio;

    private final SaqueRepositorio saqueRepositorio;

    public SaldoViewDTO depositar(SaldoFormDTO saldoFormDTO){
        UsuarioViewDTO usuario = usuarioService.buscarPorId(saldoFormDTO.getIdUsuario());

        verificarValorDeposito(saldoFormDTO.getDeposito());
        Saldo saldo = saldoRepositorio.save(SaldoEntidadeFactory.toSaldoInsertEntidade(saldoFormDTO));

        BigDecimal valorTotal = saldoTotalPorUsuario(usuario.getId());
        usuarioService.atualizarSaldoTotal(usuario.getId(),valorTotal);

        return SaldoDTOFactory.toSaldoViewDTO(saldo);
    }

    public Page<SaldoViewDTO> saldosPorUsuario(Long idUsuario, PageRequest pageRequest){
        UsuarioViewDTO usuario = usuarioService.buscarPorId(idUsuario);
        Page<Saldo> saldos = saldoRepositorio.buscarPorUsuario(usuario.getId(),pageRequest);

        return SaldoDTOFactory.toPageSaldoViewDTO(saldos);
    }

    public BigDecimal saldoTotalPorUsuario(Long idUsuario){
        List<Saldo> saldos = saldoRepositorio.findByUsuarioId(idUsuario);
        BigDecimal valorTotal = BigDecimal.ZERO;
        for(Saldo saldo : saldos){
            valorTotal = valorTotal.add(saldo.getDeposito());
        }
        List<Saque> saques = saqueRepositorio.findByUsuarioId(idUsuario);
        BigDecimal valorTotalSaqueRegistrado = BigDecimal.valueOf(saques.stream().mapToDouble(s->s.getValor().doubleValue()).sum());
        return valorTotal.subtract(valorTotalSaqueRegistrado);
    }

    private void verificarValorDeposito(BigDecimal deposito){
        if(BigDecimal.ZERO.compareTo(deposito) >= 0){
            throw new ExcecaoPersonalizada(DEPOSITO_MENOR_IGUAL_ZERO.getMensagem());
        }
    }

}
