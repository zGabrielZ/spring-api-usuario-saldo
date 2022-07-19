package br.com.gabrielferreira.spring.usuario.saldo.service;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saldo;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.SaldoFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.SaldoRepositorio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
        //Usuario usuario = usuarioService.buscarPorId(saldoFormDTO.getIdUsuario());
        //Saldo saldo = new Saldo(null,saldoFormDTO.getDeposito(),saldoFormDTO.getDataDeposito(),usuario);

        //saldo = saldoRepositorio.save(saldo);
       // usuario.adicionarSaldo(saldo);

        //BigDecimal valorTotal = saldoTotalPorUsuario(usuario);
        //usuarioService.atualizarSaldoTotal(usuario,valorTotal);

        return null;
        //return saldo;
    }

    public Page<Saldo> saldosPorUsuario(Long idUsuario, PageRequest pageRequest){
        //Usuario usuario = usuarioService.buscarPorId(idUsuario);
        //List<Saldo> saldos = saldoRepositorio.buscarPorUsuario(usuario.getId(),pageRequest);

        //int inicioConsulta = (int) pageRequest.getOffset();
        //int finalConsulta = Math.min(inicioConsulta + pageRequest.getPageSize(),saldos.size());

        return null;
        //return new PageImpl<>(saldos.subList(inicioConsulta,finalConsulta),pageRequest,saldos.size());
    }

    public BigDecimal saldoTotalPorUsuario(Usuario usuario){
        List<Saldo> saldos = usuario.getSaldos();
        BigDecimal valorTotal = BigDecimal.ZERO;
        for(Saldo saldo : saldos){
            valorTotal = valorTotal.add(saldo.getDeposito());
        }
        BigDecimal valorTotalSaqueRegistrado = BigDecimal.valueOf(usuario.getSaques().stream().mapToDouble(s->s.getValor().doubleValue()).sum());
        return valorTotal.subtract(valorTotalSaqueRegistrado);
    }

}
