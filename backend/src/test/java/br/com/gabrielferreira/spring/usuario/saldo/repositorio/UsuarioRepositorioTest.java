//package br.com.gabrielferreira.spring.usuario.saldo.repositorio;
//
//import br.com.gabrielferreira.spring.usuario.saldo.entidade.Usuario;
//import static org.assertj.core.api.Assertions.*;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.time.LocalDate;
//import java.util.Optional;
//
//@ExtendWith(SpringExtension.class)
//@ActiveProfiles("test")
//@DataJpaTest
//class UsuarioRepositorioTest {
//
//    @Autowired
//    private TestEntityManager testEntityManager;
//
//    @Autowired
//    private UsuarioRepositorio usuarioRepositorio;
//
//    @Test
//    @DisplayName("Buscar por email deveria retornar dados quando tiver dados salvos no banco de dados.")
//    void buscarPorEmail(){
//        // Cenário
//        Usuario usuario = Usuario.builder().id(null).nome("José Ferreira").email("jose@gmail.com")
//                .senha("$2a$10$rkFB6IzKB9M/T8UBxe11eOS0dsUJxxe0.R2OLhkMqFtfHdOqypwZS").cpf("73977674005")
//                .dataNascimento(LocalDate.parse("1989-02-10")).build();
//
//        testEntityManager.persist(usuario);
//
//        // Executando o método
//        Optional<Usuario> optionalUsuario = usuarioRepositorio.findByEmail("jose@gmail.com");
//
//        // Verificando
//        assertThat(optionalUsuario).isPresent();
//    }
//
//    @Test
//    @DisplayName("Buscar por email não deveria retornar dados quando não tiver dados salvos no banco de dados.")
//    void naoDeveBuscarPorEmail(){
//        // Executando o método
//        Optional<Usuario> optionalUsuario = usuarioRepositorio.findByEmail("gabriel@email.com");
//
//        // Verificando
//        assertThat(optionalUsuario).isEmpty();
//    }
//
//    @Test
//    @DisplayName("Buscar por CPF deveria retornar dados quando tiver dados salvos no banco de dados.")
//    void buscarPorCpf(){
//        // Cenário
//        Usuario usuario = Usuario.builder().id(null).nome("Gabriel Ferreira").email("gabriel@gmail.com")
//                .senha("$2a$10$rkFB6IzKB9M/T8UBxe11eOS0dsUJxxe0.R2OLhkMqFtfHdOqypwZS").cpf("01324148055")
//                .dataNascimento(LocalDate.parse("1997-12-26")).build();
//
//        testEntityManager.persist(usuario);
//
//        // Executando o método
//        Optional<Usuario> optionalUsuario = usuarioRepositorio.findByCpf("01324148055");
//
//        // Verificando
//        assertThat(optionalUsuario).isPresent();
//    }
//
//    @Test
//    @DisplayName("Buscar por CPF não deveria retornar dados quando não tiver dados salvos no banco de dados.")
//    void naoDeveBuscarPorCpf(){
//        // Executando o método
//        Optional<Usuario> optionalUsuario = usuarioRepositorio.findByCpf("01324148055");
//
//        // Verificando
//        assertThat(optionalUsuario).isEmpty();
//    }
//
//}
