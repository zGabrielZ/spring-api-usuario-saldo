package br.com.gabrielferreira.spring.usuario.saldo.repositorio;//package br.com.gabrielferreira.spring.usuario.saldo.repositorio;

import br.com.gabrielferreira.spring.usuario.saldo.entidade.Saque;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.Usuario;
import static org.assertj.core.api.Assertions.*;
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
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
class SaqueRepositorioTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private SaqueRepositorio saqueRepositorio;

    @Test
    @DisplayName("Buscar saques por usuário deveria retornar dados quando tiver registros salvos no banco de dados.")
    void buscarSaquesPorUsuario(){
        // Cenário
        Usuario usuario = Usuario.builder().id(null).nome("José Ferreira").email("jose@gmail.com")
                .senha("$2a$10$rkFB6IzKB9M/T8UBxe11eOS0dsUJxxe0.R2OLhkMqFtfHdOqypwZS").cpf("73977674005")
                .dataNascimento(LocalDate.parse("1989-02-10")).build();

        testEntityManager.persist(usuario);

        Saque saque = Saque.builder().id(null).valor(BigDecimal.valueOf(500.00)).dataSaque(LocalDateTime.now())
                .usuario(usuario).build();

        testEntityManager.persist(saque);

        // Executando o método
        List<Saque> saquesPorUsuario = saqueRepositorio.buscarPorUsuario(usuario.getId());

        // Verificando
        assertThat(saquesPorUsuario).isNotEmpty();
    }

    @Test
    @DisplayName("Buscar saques por usuário não deveria retornar dados quando não tiver registros salvos no banco de dados.")
    void naoDevebuscarSaquesPorUsuario(){
        // Executando o método
        List<Saque> saquesPorUsuario = saqueRepositorio.buscarPorUsuario(123l);

        // Verificando
        assertThat(saquesPorUsuario).isEmpty();
    }
}
