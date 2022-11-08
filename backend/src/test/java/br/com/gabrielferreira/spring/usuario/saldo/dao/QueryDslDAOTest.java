package br.com.gabrielferreira.spring.usuario.saldo.dao;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SaqueViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.QSaque;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saque;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.utils.JPAUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.*;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
class QueryDslDAOTest {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private EntityManager entityManager;

    private QueryDslDAO queryDslDAO;

    @BeforeEach
    public void beforeEach(){
        entityManager = JPAUtils.getEntityManager();
        queryDslDAO = new QueryDslDAO(entityManager);
        entityManager.getTransaction().begin();
    }

    @Test
    @DisplayName("Buscar saques por usuário deveria retornar dados quando tiver registros salvos no banco de dados.")
    void buscarSaquesPorUsuario() {
        // Cenário
        Usuario usuario = Usuario.builder().nome("José Ferreira").email("jose@gmail.com").senha("123")
                .cpf("73977674005").dataNascimento(LocalDate.parse("10/12/1995", DTF)).build();

        entityManager.persist(usuario);

        Saque saque1 = Saque.builder().valor(BigDecimal.valueOf(500.00)).dataSaque(LocalDateTime.now())
                .usuario(usuario).build();

        Saque saque2 = Saque.builder().valor(BigDecimal.valueOf(322.00)).dataSaque(LocalDateTime.now())
                .usuario(usuario).build();

        Saque saque3 = Saque.builder().valor(BigDecimal.valueOf(333.00)).dataSaque(LocalDateTime.now())
                .usuario(usuario).build();

        entityManager.persist(saque1);
        entityManager.persist(saque2);
        entityManager.persist(saque3);

        // Executando o método

        QSaque qSaque = QSaque.saque;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qSaque.usuario.id.eq(usuario.getId()));

        List<SaqueViewDTO> saques = queryDslDAO.query(q -> q.select(Projections.constructor(
                SaqueViewDTO.class,
                qSaque.id,
                qSaque.dataSaque,
                qSaque.valor
        )).from(qSaque).innerJoin(qSaque.usuario).where(booleanBuilder).fetch());

        // Verificando
        assertThat(saques).isNotEmpty();
    }

}
