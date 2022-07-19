//package br.com.gabrielferreira.spring.usuario.saldo.repositorio;
//
//import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saque;
//import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
//import static org.assertj.core.api.Assertions.*;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@ExtendWith(SpringExtension.class)
//@ActiveProfiles("test")
//@DataJpaTest
//class SaqueRepositorioTest {
//
//    @Autowired
//    private TestEntityManager testEntityManager;
//
//    @Autowired
//    private SaqueRepositorio saqueRepositorio;
//
//    @Test
//    @DisplayName("Buscar saques por usuário deveria retornar dados quando tiver registros salvos no banco de dados.")
//    void buscarSaquesPorUsuario(){
//        // Cenário
//        Usuario usuario = Usuario.builder().id(null).nome("José Ferreira").email("jose@gmail.com")
//                .senha("$2a$10$rkFB6IzKB9M/T8UBxe11eOS0dsUJxxe0.R2OLhkMqFtfHdOqypwZS").cpf("73977674005")
//                .dataNascimento(LocalDate.parse("1989-02-10")).build();
//
//        testEntityManager.persist(usuario);
//
//        Saque saque = Saque.builder().id(null).valor(BigDecimal.valueOf(500.00)).dataSaque(LocalDateTime.now())
//                .usuario(usuario).build();
//
//        testEntityManager.persist(saque);
//
//        // Executando o método
//        PageRequest pageRequest = PageRequest.of(0,1, Sort.Direction.DESC,"dataSaque");
//        List<Saque> saquesPorUsuario = saqueRepositorio.buscarPorUsuario(usuario.getId(),pageRequest);
//
//        // Verificando
//        assertThat(saquesPorUsuario).isNotEmpty();
//    }
//
//    @Test
//    @DisplayName("Buscar saques por usuário não deveria retornar dados quando não tiver registros salvos no banco de dados.")
//    void naoDevebuscarSaquesPorUsuario(){
//        // Executando o método
//        PageRequest pageRequest = PageRequest.of(0,1, Sort.Direction.DESC,"dataSaque");
//        List<Saque> saquesPorUsuario = saqueRepositorio.buscarPorUsuario(123l,pageRequest);
//
//        // Verificando
//        assertThat(saquesPorUsuario).isEmpty();
//    }
//}
