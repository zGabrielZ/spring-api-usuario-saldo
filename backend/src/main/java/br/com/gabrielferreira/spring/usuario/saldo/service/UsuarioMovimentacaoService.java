package br.com.gabrielferreira.spring.usuario.saldo.service;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Situacao;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.UsuarioMovimentacao;
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
    public void adicionarMovimentacao(Usuario usuario, BigDecimal novoSaldo, String descricao, Situacao situacao){
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

        usuarioMovimentacaoRepositorio.save(usuarioMovimentacao);
    }
}
