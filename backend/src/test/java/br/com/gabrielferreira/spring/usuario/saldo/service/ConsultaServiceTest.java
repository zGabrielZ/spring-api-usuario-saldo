package br.com.gabrielferreira.spring.usuario.saldo.service;
import br.com.gabrielferreira.spring.usuario.saldo.dao.QueryDslDAO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SaqueViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.QSaldo;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.QSaque;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.QUsuario;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;

import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
class ConsultaServiceTest {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DTFHORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @InjectMocks
    private ConsultaService consultaService;

    @Mock
    private QueryDslDAO queryDslDAO;

    @Mock(answer = Answers.RETURNS_SELF)
    private JPAQuery<SaldoViewDTO> jpaQuerySaldo;

    @Mock(answer = Answers.RETURNS_SELF)
    private JPAQuery<SaqueViewDTO> jpaQuerySaque;

    @Mock(answer = Answers.RETURNS_SELF)
    private JPAQuery<UsuarioViewDTO> jpaQueryUsuario;

    @Test
    @DisplayName("Saldos por usuário deveria retornar uma lista quando informar o usuário já cadastrado.")
    void deveMostrarSaldosPorUsuario(){
        // Cenário

        Long idUsuarioInformado = 1L;

        // Mock para retornar os saldos por usuário
        PageRequest pageRequest = PageRequest.of(0,2, Sort.Direction.DESC,"dataDeposito");
        List<SaldoViewDTO> saldoViewDTOS = saldosCriados();
        QSaldo qSaldo =  QSaldo.saldo;
        when(queryDslDAO.query(any())).thenReturn(jpaQuerySaldo);
        when(jpaQuerySaldo.from(qSaldo)).thenReturn(jpaQuerySaldo);
        when(jpaQuerySaldo.innerJoin(qSaldo.usuario)).thenReturn(jpaQuerySaldo);
        when(jpaQuerySaldo.where(any(Predicate.class))).thenReturn(jpaQuerySaldo);
        when(jpaQuerySaldo.offset(pageRequest.getOffset())).thenReturn(jpaQuerySaldo);
        when(jpaQuerySaldo.limit(pageRequest.getPageSize())).thenReturn(jpaQuerySaldo);
        when(jpaQuerySaldo.orderBy(any(OrderSpecifier.class))).thenReturn(jpaQuerySaldo);
        when(jpaQuerySaldo.fetch()).thenReturn(saldoViewDTOS);

        // Executando
        Page<SaldoViewDTO> saldosResultados = consultaService.saldosPorUsuario(idUsuarioInformado,pageRequest);

        // Verificando
        assertThat(saldosResultados).isNotEmpty();
        assertThat(saldosResultados.getTotalElements()).isEqualTo(4);
        assertThat(saldosResultados.getTotalPages()).isEqualTo(2);
        assertThat(saldosResultados.getSize()).isEqualTo(2);
    }

    @Test
    @DisplayName("Saques por usuário deveria retornar uma lista quando informar o usuário já cadastrado.")
    void deveMostrarSaquesPorUsuario(){
        // Cenário

        Long idUsuarioInformado = 1L;

        // Mock para retornar saques do usuário
        PageRequest pageRequest = PageRequest.of(0,1, Sort.Direction.DESC,"dataSaque");
        List<SaqueViewDTO> saqueViewDTOS = saquesCriados();
        QSaque qSaque =  QSaque.saque;
        when(queryDslDAO.query(any())).thenReturn(jpaQuerySaque);
        when(jpaQuerySaque.from(qSaque)).thenReturn(jpaQuerySaque);
        when(jpaQuerySaque.innerJoin(qSaque.usuario)).thenReturn(jpaQuerySaque);
        when(jpaQuerySaque.where(any(Predicate.class))).thenReturn(jpaQuerySaque);
        when(jpaQuerySaque.offset(pageRequest.getOffset())).thenReturn(jpaQuerySaque);
        when(jpaQuerySaque.limit(pageRequest.getPageSize())).thenReturn(jpaQuerySaque);
        when(jpaQuerySaque.orderBy(any(OrderSpecifier.class))).thenReturn(jpaQuerySaque);
        when(jpaQuerySaque.fetch()).thenReturn(saqueViewDTOS);

        // Executando
        Page<SaqueViewDTO> saquesResultados = consultaService.saquesPorUsuario(idUsuarioInformado,pageRequest);

        // Verificando
        assertThat(saquesResultados).isNotEmpty();
        assertThat(saquesResultados.getTotalElements()).isEqualTo(2);
        assertThat(saquesResultados.getTotalPages()).isEqualTo(2);
        assertThat(saquesResultados.getSize()).isEqualTo(1);
    }

    @Test
    @DisplayName("Deve retornar lista de usuários quando tiver dados no banco de dados.")
    void deveRetornarUsuariosPaginados(){
        // Cenário

        List<UsuarioViewDTO> usuarios = criarUsuarios();

        // Mock para retornar os dados de cima
        PageRequest pageRequest = PageRequest.of(0,2, Sort.Direction.DESC,"nome");


        // Mock
        QUsuario qUsuario =  QUsuario.usuario;
        when(queryDslDAO.query(any())).thenReturn(jpaQueryUsuario);
        when(jpaQueryUsuario.from(qUsuario)).thenReturn(jpaQueryUsuario);
        when(jpaQueryUsuario.offset(pageRequest.getOffset())).thenReturn(jpaQueryUsuario);
        when(jpaQueryUsuario.limit(pageRequest.getPageSize())).thenReturn(jpaQueryUsuario);
        when(jpaQueryUsuario.orderBy(any(OrderSpecifier.class))).thenReturn(jpaQueryUsuario);
        when(jpaQueryUsuario.fetch()).thenReturn(usuarios);

        // Execução
        Page<UsuarioViewDTO> usuarioPage = consultaService.listagem(pageRequest);

        // Verificação
        assertThat(usuarioPage.getTotalElements()).isEqualTo(3);
        assertThat(usuarioPage.getTotalPages()).isEqualTo(2);
        assertThat(usuarioPage.isEmpty()).isFalse();
    }


    private List<UsuarioViewDTO> criarUsuarios(){
        List<UsuarioViewDTO> usuarios = new ArrayList<>();
        usuarios.add(new UsuarioViewDTO(1L,"Teste","teste@email.com",null,null));
        usuarios.add(new UsuarioViewDTO(2L,"Teste 2","teste22@email.com",null,null));
        usuarios.add(new UsuarioViewDTO(3L,"Teste 3","teste33@email.com",null,null));
        return usuarios;
    }

    private List<SaldoViewDTO> saldosCriados() {
        List<SaldoViewDTO> saldos = new ArrayList<>();
        saldos.add(new SaldoViewDTO(1L,BigDecimal.valueOf(200.00),LocalDateTime.parse("30/06/2022 13:00:00", DTFHORA)));
        saldos.add(new SaldoViewDTO(2L,BigDecimal.valueOf(500.00),LocalDateTime.parse("30/06/2022 13:00:00", DTFHORA)));
        saldos.add(new SaldoViewDTO(3L,BigDecimal.valueOf(600.00),LocalDateTime.parse("30/06/2022 13:00:00", DTFHORA)));
        saldos.add(new SaldoViewDTO(4L,BigDecimal.valueOf(800.00),LocalDateTime.parse("30/06/2022 13:00:00", DTFHORA)));
        return saldos;
    }

    private List<SaqueViewDTO> saquesCriados(){
        List<SaqueViewDTO> saques = new ArrayList<>();
        saques.add(new SaqueViewDTO(1L,LocalDateTime.now(),BigDecimal.valueOf(250.00)));
        saques.add(new SaqueViewDTO(2L,LocalDateTime.now(),BigDecimal.valueOf(550.00)));
        return saques;
    }

}
