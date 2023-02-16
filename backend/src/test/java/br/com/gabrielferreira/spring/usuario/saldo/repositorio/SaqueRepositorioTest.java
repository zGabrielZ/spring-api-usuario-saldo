package br.com.gabrielferreira.spring.usuario.saldo.repositorio;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Perfil;
import br.com.gabrielferreira.spring.usuario.saldo.utils.AbstractTests;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saque;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SaqueRepositorioTest extends AbstractTests {

    @Test
    @DisplayName("Buscar lista de saques por usuário deveria retornar dados quando tiver registros salvos no banco de dados.")
    void buscarListaDeSaquesPorUsuario(){
        // Cenário
        Perfil perfilAdmin = perfilRepositorio.findById(1L).orElseThrow();
        Perfil perfilFuncionario = perfilRepositorio.findById(2L).orElseThrow();

        Usuario usuario = gerarUsuario(Arrays.asList(perfilAdmin, perfilFuncionario), "Eduardo Luiz", "eduardo@gmail.com", "123", "73016620090", LocalDate.parse("10/12/1995",DTF)
                , BigDecimal.valueOf(10000.00), false);

        testEntityManager.persist(usuario);

        Saque saque1 = gerarSaque(BigDecimal.valueOf(500.00), ZonedDateTime.now(), usuario);
        Saque saque2 = gerarSaque(BigDecimal.valueOf(600.00), ZonedDateTime.now(), usuario);
        Saque saque3 = gerarSaque(BigDecimal.valueOf(400.00), ZonedDateTime.now(), usuario);

        List<Saque> saques = Arrays.asList(saque1, saque2, saque3);
        saques.forEach(s -> testEntityManager.persist(s));

        // Executando o método
        List<BigDecimal> saquesEncontrados = saqueRepositorio.findByValorByUsuario(usuario.getId());

        // Verificando
        assertThat(saquesEncontrados).hasSize(3);
    }

    @Test
    @DisplayName("Buscar lista de saques por usuário não deveria retornar dados quando não tiver registros salvos no banco de dados.")
    void naoDeveBuscarListaDeSaquesPorUsuario(){
        // Executando o método
        List<BigDecimal> saquesEncontrados = saqueRepositorio.findByValorByUsuario(123L);

        // Verificando
        assertThat(saquesEncontrados).isEmpty();
    }
}
