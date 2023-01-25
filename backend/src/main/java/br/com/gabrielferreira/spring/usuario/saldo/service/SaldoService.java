package br.com.gabrielferreira.spring.usuario.saldo.service;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.FeriadoNacionalDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory.SaldoDTOFactory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoInsertFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoInsertResponseDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saldo;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.factory.SaldoEntidadeFactory;
import br.com.gabrielferreira.spring.usuario.saldo.client.FeriadoNacionalClient;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import br.com.gabrielferreira.spring.usuario.saldo.exception.RecursoNaoEncontrado;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.SaldoRepositorio;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.SaqueRepositorio;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;

import static br.com.gabrielferreira.spring.usuario.saldo.utils.LoginUsuarioUtils.*;
import static br.com.gabrielferreira.spring.usuario.saldo.utils.ValidacaoEnum.*;

@Service
@RequiredArgsConstructor
public class SaldoService {

    private final Clock clock;

    private final FeriadoNacionalClient nacionalClient;

    private final UsuarioRepositorio usuarioRepositorio;

    private final SaldoRepositorio saldoRepositorio;

    private final SaqueRepositorio saqueRepositorio;

    private final ConsultaService consultaService;

    private final PerfilValidacaoService perfilValidacaoService;

    @Transactional
    public SaldoInsertResponseDTO depositar(SaldoInsertFormDTO saldoInsertFormDTO){
        Usuario usuarioLogado = getRecuperarUsuarioLogado();
        perfilValidacaoService.verificarSituacaoUsuarioLogado(usuarioLogado);
        perfilValidacaoService.validarPerfilUsuarioSaldo(usuarioLogado, saldoInsertFormDTO.getIdUsuario());

        verificarValorDeposito(saldoInsertFormDTO.getDeposito());
        ZonedDateTime dataAtualDeposito = ZonedDateTime.now(clock);
        verificarDataAtualDeposito(dataAtualDeposito);
        verificarFeriadoNacional(dataAtualDeposito);

        Usuario usuarioEncontrado = usuarioRepositorio.findByIdUsuario(saldoInsertFormDTO.getIdUsuario(), false).orElseThrow(() -> new RecursoNaoEncontrado(USUARIO_NAO_ENCONTRADO.getMensagem()));
        Saldo saldo = SaldoEntidadeFactory.toSaldoInsertEntidade(saldoInsertFormDTO, ZonedDateTime.now(clock), usuarioEncontrado, usuarioLogado);
        saldo = saldoRepositorio.save(saldo);

        BigDecimal valorTotal = saldoTotalPorUsuario(usuarioEncontrado);
        usuarioEncontrado.setSaldoTotal(valorTotal);
        usuarioRepositorio.save(usuarioEncontrado);

        return SaldoDTOFactory.toSaldoInsertResonseDTO(saldo);
    }

    public BigDecimal saldoTotalPorUsuario(Usuario usuario){
        List<BigDecimal> valoresSaldos = saldoRepositorio.findByValorByUsuario(usuario.getId());
        BigDecimal valorTotal = BigDecimal.ZERO;
        for(BigDecimal saldo : valoresSaldos){
            valorTotal = valorTotal.add(saldo);
        }
        List<BigDecimal> valoresSaques = saqueRepositorio.findByValorByUsuario(usuario.getId());
        BigDecimal valorTotalSaqueRegistrado = BigDecimal.valueOf(valoresSaques.stream().mapToDouble(BigDecimal::doubleValue).sum());
        return valorTotal.subtract(valorTotalSaqueRegistrado);
    }

    public Page<SaldoViewDTO> buscarSaldosPorUsuarioPaginado(Long idUsuario, Integer pagina, Integer quantidadeRegistro, String[] sort){
        Usuario usuarioLogado = getRecuperarUsuarioLogado();
        perfilValidacaoService.verificarSituacaoUsuarioLogado(usuarioLogado);
        perfilValidacaoService.validarPerfilBuscarSaldoPorUsuario(usuarioLogado, isCliente(), idUsuario);
        return consultaService.saldosPorUsuario(idUsuario, pagina, quantidadeRegistro, sort);
    }

    private void verificarValorDeposito(BigDecimal deposito){
        if(BigDecimal.ZERO.compareTo(deposito) >= 0){
            throw new ExcecaoPersonalizada(DEPOSITO_MENOR_IGUAL_ZERO.getMensagem());
        }
    }

    private void verificarDataAtualDeposito(ZonedDateTime dataDeposito){
        DayOfWeek dayOfWeek = dataDeposito.getDayOfWeek();
        if(dayOfWeek.equals(DayOfWeek.SATURDAY) || dayOfWeek.equals(DayOfWeek.SUNDAY)){
            throw new ExcecaoPersonalizada(FINAL_DE_SEMANA.getMensagem());
        }
    }

    private void verificarFeriadoNacional(ZonedDateTime dataDeposito){
        LocalDate dataAtualDeposito = dataDeposito.toLocalDate();
        List<FeriadoNacionalDTO> feriadosNacionais = nacionalClient.buscarFeriadosNacionais(dataAtualDeposito.getYear());
        feriadosNacionais.forEach(feriadoNacionalDTO -> {
            if(feriadoNacionalDTO.getDate().equals(dataAtualDeposito)){
                throw new ExcecaoPersonalizada(FERIADO_NACIONAL.getMensagem());
            }
        });
    }

}
