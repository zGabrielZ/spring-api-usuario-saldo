package br.com.gabrielferreira.spring.usuario.saldo.utils;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Perfil;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saldo;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saque;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AbstractUtils {

    protected Usuario gerarUsuario(List<Perfil> perfis, String nome, String email, String senha, String cpf, LocalDate dataNascimento,
                                   BigDecimal saldoTotal, Boolean excluido){
        return Usuario.builder()
                .nome(nome)
                .email(email)
                .senha(senha)
                .cpf(cpf)
                .dataNascimento(dataNascimento)
                .saldoTotal(saldoTotal)
                .excluido(excluido)
                .perfis(perfis)
                .build();
    }

    protected Saque gerarSaque(BigDecimal valor, LocalDateTime dataSaque, Usuario usuario){
        return Saque.builder()
                .valor(valor)
                .dataSaque(dataSaque)
                .usuario(usuario)
                .build();
    }

    protected Saldo gerarSaldo(BigDecimal deposito, LocalDateTime dataDeposito, Usuario usuario, Usuario usuarioDepositante){
        return Saldo.builder()
                .deposito(deposito)
                .dataDeposito(dataDeposito)
                .usuario(usuario)
                .usuarioDepositante(usuarioDepositante)
                .build();
    }
}
