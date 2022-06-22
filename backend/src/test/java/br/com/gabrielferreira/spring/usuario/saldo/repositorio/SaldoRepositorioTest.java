package br.com.gabrielferreira.spring.usuario.saldo.repositorio;

import br.com.gabrielferreira.spring.usuario.saldo.entidade.Saldo;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
class SaldoRepositorioTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private SaldoRepositorio saldoRepositorio;

    @Test
    @DisplayName("Buscar saldos por usuário deveria retornar dados quando tiver registros salvos no banco de dados.")
    void buscarSaldosPorUsuario(){
        // Cenário
        Usuario usuario = Usuario.builder().id(null).nome("José Ferreira").email("jose@gmail.com")
                .senha("$2a$10$rkFB6IzKB9M/T8UBxe11eOS0dsUJxxe0.R2OLhkMqFtfHdOqypwZS").cpf("73977674005")
                .dataNascimento(LocalDate.parse("1989-02-10")).build();

        testEntityManager.persist(usuario);

        Saldo saldo = Saldo.builder().id(null).usuario(usuario).dataDeposito(LocalDateTime.now()).deposito(BigDecimal.valueOf(500.00))
                        .build();

        testEntityManager.persist(saldo);

        // Executando o método
        PageRequest pageRequest = PageRequest.of(0,1, Sort.Direction.DESC,"dataDeposito");
        List<Saldo> saldos = saldoRepositorio.buscarPorUsuario(usuario.getId(),pageRequest);

        // Verificando
        assertThat(saldos).isNotEmpty();
    }

    @Test
    @DisplayName("Buscar saldos por usuário não deveria retornar dados quando não tiver registros salvos no banco de dados.")
    void naoDeveBuscarSaldosPorUsuario(){
        // Executando o método
        PageRequest pageRequest = PageRequest.of(0,1, Sort.Direction.DESC,"dataDeposito");
        List<Saldo> saldos = saldoRepositorio.buscarPorUsuario(123l,pageRequest);

        // Verificando
        assertThat(saldos).isEmpty();
    }
}
