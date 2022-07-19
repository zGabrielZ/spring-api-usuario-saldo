package br.com.gabrielferreira.spring.usuario.saldo.service;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saque;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.SacarFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.SaqueRepositorio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import static br.com.gabrielferreira.spring.usuario.saldo.utils.ValidacaoEnum.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SaqueService {

    private final SaqueRepositorio saqueRepositorio;
    private final UsuarioService usuarioService;

    public SaqueService(SaqueRepositorio saqueRepositorio, UsuarioService usuarioService) {
        this.saqueRepositorio = saqueRepositorio;
        this.usuarioService = usuarioService;
    }

    public BigDecimal sacar(SacarFormDTO sacarFormDTO){
        Usuario usuario = usuarioService.buscarPorId(sacarFormDTO.getIdUsuario());
        verificarSaque(usuario.getSaldoTotal());
        BigDecimal saldoTotalAtual = saldoTotalUsuario(usuario.getSaldoTotal(),sacarFormDTO.getQuantidade());

        //Saque saque = new Saque(null,sacarFormDTO.getQuantidade(),LocalDateTime.now(),usuario);
        //saqueRepositorio.save(saque);
        //usuario.adicionarSaque(saque);


        usuarioService.atualizarSaldoTotal(usuario,saldoTotalAtual);

        return saldoTotalAtual;
    }

    public Page<Saque> saquesPorUsuario(Long idUsuario, PageRequest pageRequest){
        Usuario usuario = usuarioService.buscarPorId(idUsuario);
        List<Saque> saques = saqueRepositorio.buscarPorUsuario(usuario.getId(),pageRequest);

        int inicioConsulta = (int) pageRequest.getOffset();
        int finalConsulta = Math.min(inicioConsulta + pageRequest.getPageSize(),saques.size());

        return new PageImpl<>(saques.subList(inicioConsulta,finalConsulta),pageRequest,saques.size());
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
