package br.com.gabrielferreira.spring.usuario.saldo.repositorio;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
class UsuarioRepositorioTest {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Test
    @DisplayName("Buscar por email deveria retornar dado quando tiver registro salvo no banco de dados.")
    void buscarPorEmail(){
        // Cenário
        Usuario usuario = Usuario.builder().nome("José Ferreira").email("jose@gmail.com").senha("123")
                .cpf("73977674005").dataNascimento(LocalDate.parse("10/12/1995",DTF)).build();

        testEntityManager.persist(usuario);

        // Executando o método
        Optional<Usuario> optionalUsuario = usuarioRepositorio.findByEmail("jose@gmail.com");

        // Verificando
        assertThat(optionalUsuario).isPresent();
    }

    @Test
    @DisplayName("Buscar por email não deveria retornar dado quando não tiver registro salvo no banco de dados.")
    void naoDeveBuscarPorEmail(){
        // Executando o método
        Optional<Usuario> optionalUsuario = usuarioRepositorio.findByEmail("gabriel@email.com");

        // Verificando
        assertThat(optionalUsuario).isEmpty();
    }

    @Test
    @DisplayName("Buscar por CPF deveria retornar dado quando tiver registro salvo no banco de dados.")
    void buscarPorCpf(){
        // Cenário
        Usuario usuario = Usuario.builder().nome("Gabriel Ferreira").email("gabriel@gmail.com")
                .senha("123").cpf("01324148055")
                .dataNascimento(LocalDate.parse("26/12/1997",DTF)).build();

        testEntityManager.persist(usuario);

        // Executando o método
        Optional<Usuario> optionalUsuario = usuarioRepositorio.findByCpf("01324148055");

        // Verificando
        assertThat(optionalUsuario).isPresent();
    }

    @Test
    @DisplayName("Buscar por CPF não deveria retornar dado quando não tiver registro salvo no banco de dados.")
    void naoDeveBuscarPorCpf(){
        // Executando o método
        Optional<Usuario> optionalUsuario = usuarioRepositorio.findByCpf("01324148055");

        // Verificando
        assertThat(optionalUsuario).isEmpty();
    }

}
