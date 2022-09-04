//package br.com.gabrielferreira.spring.usuario.saldo.repositorio;
//import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saque;
//import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
//import static org.assertj.core.api.Assertions.*;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Arrays;
//import java.util.List;
//
//@ExtendWith(SpringExtension.class)
//@ActiveProfiles("test")
//@DataJpaTest
//class SaqueRepositorioTest {
//
//    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MM/yyyy");
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
//        Usuario usuario = Usuario.builder().nome("José Ferreira").email("jose@gmail.com").senha("123")
//                .cpf("73977674005").dataNascimento(LocalDate.parse("10/12/1995",DTF)).build();
//
//        testEntityManager.persist(usuario);
//
//        Saque saque = Saque.builder().valor(BigDecimal.valueOf(500.00)).dataSaque(LocalDateTime.now())
//                .usuario(usuario).build();
//
//        testEntityManager.persist(saque);
//
//        // Executando o método
//        PageRequest pageRequest = PageRequest.of(0,1, Sort.Direction.DESC,"dataSaque");
//        Page<Saque> saquesPorUsuario = saqueRepositorio.buscarPorUsuario(usuario.getId(),pageRequest);
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
//        Page<Saque> saquesPorUsuario = saqueRepositorio.buscarPorUsuario(123L,pageRequest);
//
//        // Verificando
//        assertThat(saquesPorUsuario).isEmpty();
//    }
//
//    @Test
//    @DisplayName("Buscar lista de saques por usuário deveria retornar dados quando tiver registros salvos no banco de dados.")
//    void buscarListaDeSaquesPorUsuario(){
//        // Cenário
//        Usuario usuario = Usuario.builder().nome("José Ferreira").email("jose@gmail.com").senha("123")
//                .cpf("73977674005").dataNascimento(LocalDate.parse("10/12/1995",DTF)).build();
//
//        testEntityManager.persist(usuario);
//
//        Saque saque1 = Saque.builder().valor(BigDecimal.valueOf(500.00)).dataSaque(LocalDateTime.now())
//                .usuario(usuario).build();
//        Saque saque2 = Saque.builder().valor(BigDecimal.valueOf(600.00)).dataSaque(LocalDateTime.now())
//                .usuario(usuario).build();
//        Saque saque3 = Saque.builder().valor(BigDecimal.valueOf(400.00)).dataSaque(LocalDateTime.now())
//                .usuario(usuario).build();
//
//        List<Saque> saques = Arrays.asList(saque1, saque2, saque3);
//        saques.forEach(s -> testEntityManager.persist(s));
//
//        // Executando o método
//        List<Saque> saquesEncontrados = saqueRepositorio.findByUsuarioId(usuario.getId());
//
//        // Verificando
//        assertThat(saquesEncontrados).hasSize(3);
//    }
//
//    @Test
//    @DisplayName("Buscar lista de saques por usuário não deveria retornar dados quando não tiver registros salvos no banco de dados.")
//    void naoDeveBuscarListaDeSaquesPorUsuario(){
//        // Executando o método
//        List<Saque> saquesEncontrados = saqueRepositorio.findByUsuarioId(123L);
//
//        // Verificando
//        assertThat(saquesEncontrados).isEmpty();
//    }
//}
