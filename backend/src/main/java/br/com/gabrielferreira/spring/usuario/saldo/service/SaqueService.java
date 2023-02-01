package br.com.gabrielferreira.spring.usuario.saldo.service;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory.SaqueDTOFactory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SacarViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SacarFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.factory.SaqueEntidadeFactory;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.SaqueRepositorio;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.UsuarioRepositorio;
import br.com.gabrielferreira.spring.usuario.saldo.utils.MascarasUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static br.com.gabrielferreira.spring.usuario.saldo.utils.LoginUsuarioUtils.getRecuperarUsuarioLogado;
import static br.com.gabrielferreira.spring.usuario.saldo.utils.ValidacaoEnum.*;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class SaqueService {

    private final Clock clock;

    private final SaqueRepositorio saqueRepositorio;

    private final UsuarioRepositorio usuarioRepositorio;

    private final PerfilValidacaoService perfilValidacaoService;

    @Transactional
    public SacarViewDTO sacar(SacarFormDTO sacarFormDTO){
        Usuario usuarioLogado = getRecuperarUsuarioLogado();
        perfilValidacaoService.verificarSituacaoUsuarioLogado(usuarioLogado);

        BigDecimal saldoTotalAtual = BigDecimal.ZERO;
        if(usuarioLogado != null){
            verificarSaque(usuarioLogado.getSaldoTotal(), sacarFormDTO.getQuantidade());
            saldoTotalAtual = saldoTotalUsuario(usuarioLogado.getSaldoTotal(), sacarFormDTO.getQuantidade());

            ZonedDateTime dataAtual = ZonedDateTime.now(clock);
            saqueRepositorio.save(SaqueEntidadeFactory.toSaqueInsertEntidade(sacarFormDTO, dataAtual, usuarioLogado));

            usuarioLogado.setSaldoTotal(saldoTotalAtual);
            usuarioRepositorio.save(usuarioLogado);
        }

        return SaqueDTOFactory.toSacarViewDTO(saldoTotalAtual);
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
