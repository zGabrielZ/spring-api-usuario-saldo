package br.com.gabrielferreira.spring.usuario.saldo.service;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory.SaqueDTOFactory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SacarViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SacarFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SaqueViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saque;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.factory.SaqueEntidadeFactory;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.SaqueRepositorio;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.UsuarioRepositorio;
import br.com.gabrielferreira.spring.usuario.saldo.utils.MascarasUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.enums.SituacaoEnum.SAQUE;
import static br.com.gabrielferreira.spring.usuario.saldo.utils.LoginUsuarioUtils.*;
import static br.com.gabrielferreira.spring.usuario.saldo.utils.LoginUsuarioUtils.isCliente;
import static br.com.gabrielferreira.spring.usuario.saldo.utils.ValidacaoEnum.*;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SaqueService {

    private final Clock clock;

    private final SaqueRepositorio saqueRepositorio;

    private final UsuarioRepositorio usuarioRepositorio;

    private final PerfilValidacaoService perfilValidacaoService;

    private final ConsultaService consultaService;

    private final SituacaoService situacaoService;

    private final UsuarioMovimentacaoService usuarioMovimentacaoService;

    @Transactional
    public SacarViewDTO sacar(SacarFormDTO sacarFormDTO){
        Usuario usuarioLogado = getRecuperarUsuarioLogado();

        BigDecimal saldoTotalAtual = BigDecimal.ZERO;
        if(usuarioLogado != null){
            verificarSaque(usuarioLogado.getSaldoTotal(), sacarFormDTO.getQuantidade());
            saldoTotalAtual = saldoTotalUsuario(usuarioLogado.getSaldoTotal(), sacarFormDTO.getQuantidade());

            LocalDateTime dataAtual = LocalDateTime.now(clock);
            Saque saque = SaqueEntidadeFactory.toSaqueInsertEntidade(sacarFormDTO, dataAtual, usuarioLogado);
            saqueRepositorio.save(saque);

            usuarioLogado.setSaldoTotal(saldoTotalAtual);
            usuarioRepositorio.save(usuarioLogado);

            var situacao = situacaoService.buscarPorCodigo(SAQUE.name());
            var descricao = String.format("Sacando um novo valor para o %s", usuarioLogado.getNome());
            usuarioMovimentacaoService.adicionarMovimentacao(usuarioLogado, sacarFormDTO.getQuantidade(), descricao, situacao, saque);
        }

        return SaqueDTOFactory.toSacarViewDTO(saldoTotalAtual);
    }

    public Page<SaqueViewDTO> buscarSaquesPorUsuarioPaginado(Long idUsuario, Integer pagina, Integer quantidadeRegistro, String[] sort){
        Usuario usuarioLogado = getRecuperarUsuarioLogado();
        perfilValidacaoService.validarPerfilBuscarSaquePorUsuario(usuarioLogado, idUsuario, isAdmin(), isFuncionario(), isCliente());
        return consultaService.saquesPorUsuario(idUsuario, pagina, quantidadeRegistro, sort);
    }

    private void verificarSaque(BigDecimal saldoTotal, BigDecimal saque){
        if(saldoTotal == null || saldoTotal.compareTo(BigDecimal.ZERO) == 0){
            throw new ExcecaoPersonalizada(SAQUE_NAO_ENCONTRADO.getMensagem());
        } else if(saque.compareTo(BigDecimal.ZERO) <= 0){
            throw new ExcecaoPersonalizada(SAQUE_MENOR_IGUAL_ZERO.getMensagem());
        }
    }

    private BigDecimal saldoTotalUsuario(BigDecimal saldoTotal, BigDecimal quantidade){
        if(quantidade.compareTo(saldoTotal) > 0){
            throw new ExcecaoPersonalizada(SALDO_TOTAL_USUARIO.getMensagem() + MascarasUtils.toSaldoFormatado(saldoTotal));
        }
        return saldoTotal.subtract(quantidade);
    }

}
