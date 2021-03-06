package br.com.gabrielferreira.spring.usuario.saldo.service;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.FeriadoNacionalDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory.SaldoDTOFactory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saldo;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saque;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.factory.SaldoEntidadeFactory;
import br.com.gabrielferreira.spring.usuario.saldo.client.FeriadoNacionalClient;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.SaldoRepositorio;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.SaqueRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static br.com.gabrielferreira.spring.usuario.saldo.utils.ValidacaoEnum.*;

@Service
@RequiredArgsConstructor
public class SaldoService {

    private final Clock clock;

    private final FeriadoNacionalClient nacionalClient;

    private final UsuarioService usuarioService;

    private final SaldoRepositorio saldoRepositorio;

    private final SaqueRepositorio saqueRepositorio;

    @Transactional
    public SaldoViewDTO depositar(SaldoFormDTO saldoFormDTO){
        UsuarioViewDTO usuario = usuarioService.buscarPorId(saldoFormDTO.getIdUsuario());

        verificarValorDeposito(saldoFormDTO.getDeposito());
        Saldo saldo = SaldoEntidadeFactory.toSaldoInsertEntidade(saldoFormDTO, LocalDateTime.now(clock));
        verificarDataAtualDeposito(saldo.getDataDeposito());
        verificarFeriadoNacional(saldo.getDataDeposito());
        saldo = saldoRepositorio.save(saldo);

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

    private void verificarDataAtualDeposito(LocalDateTime dataDeposito){
        DayOfWeek dayOfWeek = dataDeposito.getDayOfWeek();
        if(dayOfWeek.equals(DayOfWeek.SATURDAY) || dayOfWeek.equals(DayOfWeek.SUNDAY)){
            throw new ExcecaoPersonalizada(FINAL_DE_SEMANA.getMensagem());
        }
    }

    private void verificarFeriadoNacional(LocalDateTime dataDeposito){
        LocalDate dataAtualDeposito = dataDeposito.toLocalDate();
        List<FeriadoNacionalDTO> feriadosNacionais = nacionalClient.buscarFeriadosNacionais();
        feriadosNacionais.forEach(feriadoNacionalDTO -> {
            if(feriadoNacionalDTO.getDate().equals(dataAtualDeposito)){
                throw new ExcecaoPersonalizada(FERIADO_NACIONAL.getMensagem());
            }
        });
    }

}
