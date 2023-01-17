package br.com.gabrielferreira.spring.usuario.saldo.service;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory.SaqueDTOFactory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoTotalViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SacarViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SacarFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.factory.SaqueEntidadeFactory;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.SaqueRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static br.com.gabrielferreira.spring.usuario.saldo.utils.ValidacaoEnum.*;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SaqueService {

    private final Clock clock;

    private final SaqueRepositorio saqueRepositorio;

    private final UsuarioService usuarioService;

    private final PerfilService perfilService;

    @Transactional
    public SacarViewDTO sacar(SacarFormDTO sacarFormDTO){

        verificarUsuarioLogado(sacarFormDTO.getIdUsuario());

        SaldoTotalViewDTO saldoTotalViewDTO = usuarioService.buscarSaldoTotal(sacarFormDTO.getIdUsuario());
        verificarSaque(saldoTotalViewDTO.getSaldoTotal(), sacarFormDTO.getQuantidade());
        BigDecimal saldoTotalAtual = saldoTotalUsuario(saldoTotalViewDTO.getSaldoTotal(),sacarFormDTO.getQuantidade());

        LocalDateTime dataAtual = LocalDateTime.now(clock);
        saqueRepositorio.save(SaqueEntidadeFactory.toSaqueInsertEntidade(sacarFormDTO, dataAtual));

        BigDecimal saldoTotal = usuarioService.atualizarSaldoTotal(sacarFormDTO.getIdUsuario(),saldoTotalAtual);

        return SaqueDTOFactory.toSacarViewDTO(saldoTotal);
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
            throw new ExcecaoPersonalizada(SALDO_TOTAL_USUARIO.getMensagem() + saldoTotal);
        }
        return saldoTotal.subtract(quantidade);
    }

    private void verificarUsuarioLogado(Long idUsuario){
//        Usuario usuarioLogado = perfilService.recuperarUsuarioLogado();
//        if(!usuarioLogado.getId().equals(idUsuario)){
//            throw new ExcecaoPersonalizada(SAQUE_CONTA_PROPRIA.getMensagem());
//        }
    }
}
