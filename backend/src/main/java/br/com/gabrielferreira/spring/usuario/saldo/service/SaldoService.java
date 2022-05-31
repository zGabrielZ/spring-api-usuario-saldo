package br.com.gabrielferreira.spring.usuario.saldo.service;

import br.com.gabrielferreira.spring.usuario.saldo.entidade.Saldo;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.dto.SacarFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.dto.SaldoFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import br.com.gabrielferreira.spring.usuario.saldo.exception.RecursoNaoEncontrado;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.SaldoRepositorio;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SaldoService {

    private final SaldoRepositorio saldoRepositorio;

    private final UsuarioService usuarioService;

    public SaldoService(SaldoRepositorio saldoRepositorio, UsuarioService usuarioService) {
        this.saldoRepositorio = saldoRepositorio;
        this.usuarioService = usuarioService;
    }

    public Saldo depositar(SaldoFormDTO saldoFormDTO){
        Usuario usuario = usuarioService.buscarPorId(saldoFormDTO.getIdUsuario());
        Saldo saldo = new Saldo(null,saldoFormDTO.getDeposito(),saldoFormDTO.getDataDeposito(),usuario);
        verificarDeposito(saldo.getDeposito());

        saldoRepositorio.save(saldo);
        usuario.adicionarSaldo(saldo);

        BigDecimal valorTotal = saldoTotalPorUsuario(usuario);
        usuarioService.atualizarSaldoTotal(usuario,valorTotal);


        return saldo;
    }

    public BigDecimal sacar(SacarFormDTO sacarFormDTO){
        Usuario usuario = usuarioService.buscarPorId(sacarFormDTO.getIdUsuario());
        verificarSaque(usuario.getSaldoTotal());
        BigDecimal saldoTotalAtual = saldoTotalUsuario(usuario.getSaldoTotal(),sacarFormDTO.getQuantidade());
        usuarioService.atualizarSaldoTotal(usuario,saldoTotalAtual);
        return saldoTotalAtual;
    }

    public List<Saldo> saldosPorUsuario(Long idUsuario){
        Usuario usuario = usuarioService.buscarPorId(idUsuario);
        List<Saldo> saldos = usuario.getSaldos();
        if(saldos.isEmpty()){
            throw new RecursoNaoEncontrado("Nenhum saldo foi encontrado para o usuário " + usuario.getNome());
        }
        return saldos;
    }

    public BigDecimal saldoTotalPorUsuario(Usuario usuario){
        List<Saldo> saldos = usuario.getSaldos();
        BigDecimal valorTotal = BigDecimal.ZERO;
        for(Saldo saldo : saldos){
            valorTotal = valorTotal.add(saldo.getDeposito());
        }
        return valorTotal;
    }

    private void verificarDeposito(BigDecimal deposito){
        if(deposito.doubleValue() <= 0.0){
            throw new ExcecaoPersonalizada("O déposito não pode ser menor ou igual ao 0.");
        }
    }

    private void verificarSaque(BigDecimal saldoTotal){
        if(saldoTotal == null || saldoTotal.equals(BigDecimal.ZERO)){
            throw new ExcecaoPersonalizada("Não é possível sacar sem nenhum valor.");
        }
    }

    private BigDecimal saldoTotalUsuario(BigDecimal saldoTotal, BigDecimal quantidade){
        if(quantidade.doubleValue() > saldoTotal.doubleValue()){
            throw new ExcecaoPersonalizada("Não é possível sacar pois o saldo total é " + saldoTotal);
        }
        return saldoTotal.subtract(quantidade);
    }

}
