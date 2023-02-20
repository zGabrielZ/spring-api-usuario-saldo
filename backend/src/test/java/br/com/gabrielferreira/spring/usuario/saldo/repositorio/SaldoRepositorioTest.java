package br.com.gabrielferreira.spring.usuario.saldo.repositorio;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Perfil;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saldo;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.utils.AbstractUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static br.com.gabrielferreira.spring.usuario.saldo.utils.MascarasUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SaldoRepositorioTest extends AbstractUtils {

    @Autowired
    PerfilRepositorio perfilRepositorio;

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    SaldoRepositorio saldoRepositorio;

    @Test
    @DisplayName("Buscar lista de saldos por usuário deveria retornar dados quando tiver registros salvos no banco de dados.")
    void buscarListaDeSaldosPorUsuario(){
        // Cenário
        Perfil perfilAdmin = perfilRepositorio.findById(1L).orElseThrow();
        Perfil perfilFuncionario = perfilRepositorio.findById(2L).orElseThrow();

        Usuario usuario = gerarUsuario(Arrays.asList(perfilAdmin, perfilFuncionario), "José Ferreira", "jose@gmail.com", "123", "73977674005", LocalDate.parse("10/12/1995",DATA_FORMATTER)
                , BigDecimal.valueOf(10000.00), false);

        testEntityManager.persist(usuario);

        Usuario usuario2 = gerarUsuario(Arrays.asList(perfilAdmin, perfilFuncionario), "Marcos da Silva", "marcos@gmail.com", "123", "56038504001", LocalDate.parse("05/01/2000",DATA_FORMATTER)
                , BigDecimal.valueOf(10000.00), false);

        testEntityManager.persist(usuario2);

        Saldo saldo1 = gerarSaldo(BigDecimal.valueOf(500.00), LocalDateTime.now(), usuario, usuario2);
        Saldo saldo2 = gerarSaldo(BigDecimal.valueOf(800.00), LocalDateTime.now(), usuario, usuario2);
        Saldo saldo3 = gerarSaldo(BigDecimal.valueOf(400.00), LocalDateTime.now(), usuario, usuario2);

        List<Saldo> saldos = Arrays.asList(saldo1, saldo2, saldo3);
        saldos.forEach(s -> testEntityManager.persist(s));

        // Executando o método
        List<BigDecimal> saldosEncontrados = saldoRepositorio.findByValorByUsuario(usuario.getId());

        // Verificando
        assertThat(saldosEncontrados).hasSize(3);
    }

    @Test
    @DisplayName("Buscar lista de saldos por usuário não deveria retornar dados quando não tiver registros salvos no banco de dados.")
    void naoDeveBuscarListaDeSaldosPorUsuario(){
        // Executando o método
        List<BigDecimal> saldosEncontrados = saldoRepositorio.findByValorByUsuario(123L);

        // Verificando
        assertThat(saldosEncontrados).isEmpty();
    }
}
