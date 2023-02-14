package br.com.gabrielferreira.spring.usuario.saldo.repositorio;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Perfil;
import br.com.gabrielferreira.spring.usuario.saldo.utils.AbstractTests;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saque;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import static org.assertj.core.api.Assertions.*;
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
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SaqueRepositorioTest extends AbstractTests {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private SaqueRepositorio saqueRepositorio;

    @Autowired
    private PerfilRepositorio perfilRepositorio;

    @Test
    @DisplayName("Buscar lista de saques por usuário deveria retornar dados quando tiver registros salvos no banco de dados.")
    void buscarListaDeSaquesPorUsuario(){
        // Cenário
        Perfil perfilAdmin = perfilRepositorio.findById(1L).orElseThrow();

        Usuario usuario = Usuario.builder().nome("Eduardo Luiz").email("eduardo@gmail.com").senha("123")
                .cpf("73016620090").dataNascimento(LocalDate.parse("10/12/1995",DTF))
                .dataInclusao(ZonedDateTime.now()).excluido(false)
                .perfis(List.of(perfilAdmin))
                .saldoTotal(BigDecimal.ZERO)
                .build();

        testEntityManager.persist(usuario);

        Saque saque1 = Saque.builder().valor(BigDecimal.valueOf(500.00)).dataSaque(ZonedDateTime.now())
                .usuario(usuario).build();
        Saque saque2 = Saque.builder().valor(BigDecimal.valueOf(600.00)).dataSaque(ZonedDateTime.now())
                .usuario(usuario).build();
        Saque saque3 = Saque.builder().valor(BigDecimal.valueOf(400.00)).dataSaque(ZonedDateTime.now())
                .usuario(usuario).build();

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
