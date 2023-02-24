package br.com.gabrielferreira.spring.usuario.saldo.service;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Situacao;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.UsuarioMovimentacao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UsuarioMovimentacaoService {

    public void adicionarMovimentacao(Usuario usuario, BigDecimal novoSaldo, String descricao, Situacao situacao){
        BigDecimal variacaoNovoSaldoComSaldoTotal = novoSaldo.subtract(usuario.getSaldoTotal());
        BigDecimal quantidadeInformada = usuario.getSaldoTotal().add(variacaoNovoSaldoComSaldoTotal);

        UsuarioMovimentacao usuarioMovimentacao = UsuarioMovimentacao.builder()
                .usuario(usuario)
                .saldoAtual(usuario.getSaldoTotal())
                .quantidadeInformada(quantidadeInformada)
                .descricao(descricao)
                .situacao(situacao)
                .build();

        usuario.getMovimentacoes().add(usuarioMovimentacao);
    }
}
