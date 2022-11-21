package br.com.gabrielferreira.spring.usuario.saldo.repositorio;
import br.com.gabrielferreira.spring.usuario.saldo.utils.AbstractTests;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saldo;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
class SaldoRepositorioTest extends AbstractTests {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private SaldoRepositorio saldoRepositorio;

    @Test
    @DisplayName("Buscar lista de saldos por usuário deveria retornar dados quando tiver registros salvos no banco de dados.")
    void buscarListaDeSaldosPorUsuario(){
        // Cenário
        Usuario usuario = Usuario.builder().nome("José Ferreira").email("jose@gmail.com").senha("123")
                .cpf("73977674005").dataNascimento(LocalDate.parse("10/12/1995",DTF)).build();

        testEntityManager.persist(usuario);

        Saldo saldo1 = Saldo.builder().usuario(usuario).dataDeposito(LocalDateTime.now()).deposito(BigDecimal.valueOf(500.00))
                .build();
        Saldo saldo2 = Saldo.builder().usuario(usuario).dataDeposito(LocalDateTime.now()).deposito(BigDecimal.valueOf(800.00))
                .build();
        Saldo saldo3 = Saldo.builder().usuario(usuario).dataDeposito(LocalDateTime.now()).deposito(BigDecimal.valueOf(400.00))
                .build();

        List<Saldo> saldos = Arrays.asList(saldo1, saldo2, saldo3);
        saldos.forEach(s -> testEntityManager.persist(s));

        // Executando o método
        List<Saldo> saldosEncontrados = saldoRepositorio.findByUsuarioId(usuario.getId());

        // Verificando
        assertThat(saldosEncontrados).hasSize(3);
    }

    @Test
    @DisplayName("Buscar lista de saldos por usuário não deveria retornar dados quando não tiver registros salvos no banco de dados.")
    void naoDeveBuscarListaDeSaldosPorUsuario(){
        // Executando o método
        List<Saldo> saldosEncontrados = saldoRepositorio.findByUsuarioId(123L);

        // Verificando
        assertThat(saldosEncontrados).isEmpty();
    }
}
