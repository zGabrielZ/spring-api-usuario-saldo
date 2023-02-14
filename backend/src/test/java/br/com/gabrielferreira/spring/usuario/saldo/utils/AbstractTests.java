package br.com.gabrielferreira.spring.usuario.saldo.utils;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Perfil;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saldo;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saque;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public abstract class AbstractTests {

    protected static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//    protected static final DateTimeFormatter DTFHORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
//    protected static final String PORTA = "http://localhost:8080";
//
//    protected static final String ROLE_ADMIN = "ROLE_ADMIN";
//
//    protected static final String ROLE_FUNCIONARIO = "ROLE_FUNCIONARIO";
//
//    protected static final String ROLE_CLIENTE = "ROLE_CLIENTE";
//
//    protected Usuario gerarUsuarioLogado(Long idPerfil, String nomePerfil, Long idUsuario, String nomeUsuario){
//        Perfil perfil = Perfil.builder().id(idPerfil).nome(nomePerfil).build();
//
//        return Usuario.builder().id(idUsuario).nome(nomeUsuario)
//                .saldoTotal(BigDecimal.ZERO)
//                .perfis(List.of(perfil))
//                .build();
//    }

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
