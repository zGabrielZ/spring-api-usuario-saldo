package br.com.gabrielferreira.spring.usuario.saldo.repositorio;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Perfil;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import static org.assertj.core.api.Assertions.*;

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
import java.util.Arrays;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UsuarioRepositorioTest extends AbstractUtils {

    @Autowired
    PerfilRepositorio perfilRepositorio;

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Test
    @DisplayName("Buscar por email deveria retornar dado quando tiver registro salvo no banco de dados.")
    void buscarPorEmail(){
        // Cenário
        Perfil perfilAdmin = perfilRepositorio.findById(1L).orElseThrow();
        Perfil perfilFuncionario = perfilRepositorio.findById(2L).orElseThrow();

        Usuario usuario = gerarUsuario(Arrays.asList(perfilAdmin, perfilFuncionario), "Marinho da Silva", "marinho@gmail.com", "123", "41194800033", LocalDate.parse("10/12/1995",DTF)
                , BigDecimal.valueOf(10000.00), false);

        testEntityManager.persist(usuario);

        // Executando o método
        Optional<Usuario> optionalUsuario = usuarioRepositorio.findByEmail("marinho@gmail.com");

        // Verificando
        assertThat(optionalUsuario).isPresent();
    }

    @Test
    @DisplayName("Buscar por email não deveria retornar dado quando não tiver registro salvo no banco de dados.")
    void naoDeveBuscarPorEmail(){
        // Executando o método
        Optional<Usuario> optionalUsuario = usuarioRepositorio.findByEmail("seila@email.com");

        // Verificando
        assertThat(optionalUsuario).isEmpty();
    }

    @Test
    @DisplayName("Existe email deveria retornar dado quando tiver registro salvo no banco de dados.")
    void existePorEmail(){
        // Cenário
        Perfil perfilAdmin = perfilRepositorio.findById(1L).orElseThrow();
        Perfil perfilFuncionario = perfilRepositorio.findById(2L).orElseThrow();

        Usuario usuario = gerarUsuario(Arrays.asList(perfilAdmin, perfilFuncionario), "Josué da Silva", "josuee@gmail.com", "123", "07188077070", LocalDate.parse("10/12/1995",DTF)
                , BigDecimal.valueOf(10000.00), false);

        testEntityManager.persist(usuario);

        // Executando o método
        Optional<String> optionalEmail = usuarioRepositorio.existsEmail("josuee@gmail.com");

        // Verificando
        assertThat(optionalEmail).isPresent();
    }

    @Test
    @DisplayName("Existe cpf deveria retornar dado quando tiver registro salvo no banco de dados.")
    void existePorCpf(){
        // Cenário
        Perfil perfilAdmin = perfilRepositorio.findById(1L).orElseThrow();
        Perfil perfilFuncionario = perfilRepositorio.findById(2L).orElseThrow();

        Usuario usuario = gerarUsuario(Arrays.asList(perfilAdmin, perfilFuncionario), "Mariana da Silva", "mariana@gmail.com", "123", "88013832074", LocalDate.parse("10/12/1995",DTF)
                , BigDecimal.valueOf(10000.00), false);

        testEntityManager.persist(usuario);

        // Executando o método
        Optional<String> optionalCpf = usuarioRepositorio.existsCpf("88013832074");

        // Verificando
        assertThat(optionalCpf).isPresent();
    }

    @Test
    @DisplayName("Buscar por usuario id deveria retornar dado quando tiver registro salvo no banco de dados.")
    void buscarPorUsuario(){
        // Cenário
        Perfil perfilAdmin = perfilRepositorio.findById(1L).orElseThrow();
        Perfil perfilFuncionario = perfilRepositorio.findById(2L).orElseThrow();

        Usuario usuario = gerarUsuario(Arrays.asList(perfilAdmin, perfilFuncionario), "Luana da Silva", "luana@gmail.com", "123", "11824188056", LocalDate.parse("10/12/1995",DTF)
                , BigDecimal.valueOf(10000.00), false);

        testEntityManager.persist(usuario);

        // Executando o método
        Optional<Usuario> optionalUsuario = usuarioRepositorio.findByIdUsuario(usuario.getId());

        // Verificando
        assertThat(optionalUsuario).isPresent();
    }

}
