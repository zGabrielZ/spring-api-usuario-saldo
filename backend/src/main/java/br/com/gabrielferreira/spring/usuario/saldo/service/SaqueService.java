package br.com.gabrielferreira.spring.usuario.saldo.service;

import br.com.gabrielferreira.spring.usuario.saldo.entidade.Saque;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.dto.SacarFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.SaqueRepositorio;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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

        Saque saque = new Saque(null,sacarFormDTO.getQuantidade(),usuario);
        saqueRepositorio.save(saque);
        usuario.adicionarSaque(saque);


        usuarioService.atualizarSaldoTotal(usuario,saldoTotalAtual);

        return saldoTotalAtual;
    }

    private void verificarSaque(BigDecimal saldoTotal){
        if(saldoTotal == null || saldoTotal.compareTo(BigDecimal.ZERO) == 0){
            throw new ExcecaoPersonalizada("Não é possível sacar sem nenhum valor.");
        }
    }

    private BigDecimal saldoTotalUsuario(BigDecimal saldoTotal, BigDecimal quantidade){
        if(quantidade.compareTo(saldoTotal) > 0){
            throw new ExcecaoPersonalizada("Não é possível sacar pois o saldo total é " + saldoTotal);
        }
        return saldoTotal.subtract(quantidade);
    }
}
