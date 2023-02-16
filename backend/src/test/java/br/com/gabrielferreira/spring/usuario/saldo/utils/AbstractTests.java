package br.com.gabrielferreira.spring.usuario.saldo.utils;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Perfil;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saldo;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saque;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.PerfilRepositorio;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.SaldoRepositorio;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.SaqueRepositorio;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public abstract class AbstractTests {

    protected static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    protected static final DateTimeFormatter DTFHORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
//    protected static final String PORTA = "http://localhost:8080";

    @Autowired
    protected TestEntityManager testEntityManager;

    @Autowired
    protected SaldoRepositorio saldoRepositorio;

    @Autowired
    protected PerfilRepositorio perfilRepositorio;

    @Autowired
    protected SaqueRepositorio saqueRepositorio;

    @Autowired
    protected UsuarioRepositorio usuarioRepositorio;

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

    protected Saque gerarSaque(BigDecimal valor, ZonedDateTime dataSaque, Usuario usuario){
        return Saque.builder()
                .valor(valor)
                .dataSaque(dataSaque)
                .usuario(usuario)
                .build();
    }

    protected Saldo gerarSaldo(BigDecimal deposito, ZonedDateTime dataDeposito, Usuario usuario, Usuario usuarioDepositante){
        return Saldo.builder()
                .deposito(deposito)
                .dataDeposito(dataDeposito)
                .usuario(usuario)
                .usuarioDepositante(usuarioDepositante)
                .build();
    }
}
