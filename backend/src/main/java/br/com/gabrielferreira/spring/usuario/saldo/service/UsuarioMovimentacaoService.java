package br.com.gabrielferreira.spring.usuario.saldo.service;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.*;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.UsuarioMovimentacaoRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

import static br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.enums.SituacaoEnum.*;

@Service
@RequiredArgsConstructor
public class UsuarioMovimentacaoService {

    private final UsuarioMovimentacaoRepositorio usuarioMovimentacaoRepositorio;

    @Transactional
    public void adicionarMovimentacao(Usuario usuario, BigDecimal novoSaldo, String descricao, Situacao situacao, Object obj){
        BigDecimal variacaoNovoSaldoComSaldoTotal = novoSaldo.subtract(usuario.getSaldoTotal());
        BigDecimal quantidadeInformada = usuario.getSaldoTotal().add(variacaoNovoSaldoComSaldoTotal);

        if(situacao.getCodigo().equals(SAQUE.name())){
            quantidadeInformada = quantidadeInformada.multiply(BigDecimal.valueOf(-1.0));
        }

        UsuarioMovimentacao usuarioMovimentacao = UsuarioMovimentacao.builder()
                .usuario(usuario)
                .saldoAtual(usuario.getSaldoTotal())
                .quantidadeInformada(quantidadeInformada)
                .descricao(descricao)
                .situacao(situacao)
                .build();

        if(obj instanceof Saldo saldo){
            usuarioMovimentacao.setSaldo(saldo);
        } else {
            usuarioMovimentacao.setSaque((Saque) obj);
        }

        usuarioMovimentacaoRepositorio.save(usuarioMovimentacao);
    }
}
