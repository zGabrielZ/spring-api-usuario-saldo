package br.com.gabrielferreira.spring.usuario.saldo.service;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory.SaqueDTOFactory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoTotalViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SacarViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SaqueViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saque;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SacarFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.factory.SaqueEntidadeFactory;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.SaqueRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import static br.com.gabrielferreira.spring.usuario.saldo.utils.ValidacaoEnum.*;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SaqueService {

    private final SaqueRepositorio saqueRepositorio;

    private final UsuarioService usuarioService;

    public SacarViewDTO sacar(SacarFormDTO sacarFormDTO){
        SaldoTotalViewDTO saldoTotalViewDTO = usuarioService.buscarSaldoTotal(sacarFormDTO.getIdUsuario());
        verificarSaque(saldoTotalViewDTO.getSaldoTotal());
        BigDecimal saldoTotalAtual = saldoTotalUsuario(saldoTotalViewDTO.getSaldoTotal(),sacarFormDTO.getQuantidade());

        saqueRepositorio.save(SaqueEntidadeFactory.toSaqueInsertEntidade(sacarFormDTO));

        BigDecimal saldoTotal = usuarioService.atualizarSaldoTotal(sacarFormDTO.getIdUsuario(),saldoTotalAtual);

        return SaqueDTOFactory.toSacarViewDTO(saldoTotal);
    }

    public Page<SaqueViewDTO> saquesPorUsuario(Long idUsuario, PageRequest pageRequest){
        UsuarioViewDTO usuario = usuarioService.buscarPorId(idUsuario);
        Page<Saque> saques = saqueRepositorio.buscarPorUsuario(usuario.getId(),pageRequest);

        return SaqueDTOFactory.toPageSaldoViewDTO(saques);
    }

    private void verificarSaque(BigDecimal saldoTotal){
        if(saldoTotal == null || saldoTotal.compareTo(BigDecimal.ZERO) == 0){
            throw new ExcecaoPersonalizada(SAQUE_NAO_ENCONTRADO.getMensagem());
        }
    }

    private BigDecimal saldoTotalUsuario(BigDecimal saldoTotal, BigDecimal quantidade){
        if(quantidade.compareTo(saldoTotal) > 0){
            throw new ExcecaoPersonalizada(SALDO_TOTAL_USUARIO.getMensagem() + saldoTotal);
        }
        return saldoTotal.subtract(quantidade);
    }
}
