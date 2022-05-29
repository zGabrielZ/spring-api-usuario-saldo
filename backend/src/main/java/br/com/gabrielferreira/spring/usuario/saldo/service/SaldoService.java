package br.com.gabrielferreira.spring.usuario.saldo.service;

import br.com.gabrielferreira.spring.usuario.saldo.entidade.Saldo;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.dto.SaldoFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.SaldoRepositorio;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class SaldoService {

    private final SaldoRepositorio saldoRepositorio;

    private final UsuarioService usuarioService;

    public SaldoService(SaldoRepositorio saldoRepositorio, UsuarioService usuarioService) {
        this.saldoRepositorio = saldoRepositorio;
        this.usuarioService = usuarioService;
    }

    public Saldo inserir(SaldoFormDTO saldoFormDTO){
        Usuario usuario = usuarioService.buscarPorId(saldoFormDTO.getIdUsuario());
        Saldo saldo = new Saldo(null,saldoFormDTO.getDeposito(),saldoFormDTO.getDataDeposito(),usuario);
        verificarDeposito(saldo.getDeposito());
        return saldoRepositorio.save(saldo);
    }

    private void verificarDeposito(BigDecimal deposito){
        if(deposito.doubleValue() <= 0.0){
            throw new ExcecaoPersonalizada("O déposito não pode ser menor ou igual ao 0.");
        }
    }

}
