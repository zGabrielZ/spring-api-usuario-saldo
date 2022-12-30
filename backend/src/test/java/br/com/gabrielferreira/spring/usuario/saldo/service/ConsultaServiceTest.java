//package br.com.gabrielferreira.spring.usuario.saldo.service;
//import br.com.gabrielferreira.spring.usuario.saldo.utils.AbstractTests;
//import br.com.gabrielferreira.spring.usuario.saldo.dao.QueryDslDAO;
//import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoViewDTO;
//import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SaqueViewDTO;
//import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioViewDTO;
//import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Perfil;
//import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.QSaldo;
//import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.QSaque;
//import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
//import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
//import br.com.gabrielferreira.spring.usuario.saldo.repositorio.UsuarioRepositorio;
//import com.querydsl.core.types.OrderSpecifier;
//import com.querydsl.core.types.Predicate;
//import com.querydsl.jpa.impl.JPAQuery;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import static org.mockito.Mockito.*;
//
//import org.mockito.Answers;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.data.domain.*;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import static org.assertj.core.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.*;
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@ExtendWith(SpringExtension.class)
//class ConsultaServiceTest extends AbstractTests {
//
//    @InjectMocks
//    private ConsultaService consultaService;
//
//    @Mock
//    private PerfilService perfilService;
//
//    @Mock
//    private UsuarioRepositorio usuarioRepositorio;
//
//    @Mock
//    private QueryDslDAO queryDslDAO;
//
//    @Mock(answer = Answers.RETURNS_SELF)
//    private JPAQuery<SaldoViewDTO> jpaQuerySaldo;
//
//    @Mock(answer = Answers.RETURNS_SELF)
//    private JPAQuery<SaqueViewDTO> jpaQuerySaque;
//
//    @Mock(answer = Answers.RETURNS_SELF)
//    private JPAQuery<UsuarioViewDTO> jpaQueryUsuario;
//
//    @Test
//    @DisplayName("Saldos por usuário deveria retornar uma lista quando informar o usuário já cadastrado.")
//    void deveMostrarSaldosPorUsuario(){
//        // Cenário
//
//        // Recuperar Usuário logado como Admin
//        when(perfilService.recuperarUsuarioLogado()).thenReturn(gerarUsuarioLogado(1L, ROLE_ADMIN, 1L, "Gabriel Ferreira"));
//
//        // Verificar Usuário logado como Admin
//        when(perfilService.isContemPerfilClienteUsuarioLogado()).thenReturn(false);
//
//        // Usuário informado
//        Long idUsuarioInformado = 1L;
//
//        // Mock para retornar os saldos por usuário
//        PageRequest pageRequest = PageRequest.of(0,2, Sort.Direction.DESC,"dataDeposito");
//        List<SaldoViewDTO> saldoViewDTOS = saldosCriados();
//        QSaldo qSaldo =  QSaldo.saldo;
//        when(queryDslDAO.query(any())).thenReturn(jpaQuerySaldo);
//        when(jpaQuerySaldo.from(qSaldo)).thenReturn(jpaQuerySaldo);
//        when(jpaQuerySaldo.innerJoin(qSaldo.usuario)).thenReturn(jpaQuerySaldo);
//        when(jpaQuerySaldo.where(any(Predicate.class))).thenReturn(jpaQuerySaldo);
//        when(jpaQuerySaldo.offset(pageRequest.getOffset())).thenReturn(jpaQuerySaldo);
//        when(jpaQuerySaldo.limit(pageRequest.getPageSize())).thenReturn(jpaQuerySaldo);
//        when(jpaQuerySaldo.orderBy(any(OrderSpecifier.class))).thenReturn(jpaQuerySaldo);
//        when(jpaQuerySaldo.fetch()).thenReturn(saldoViewDTOS);
//
//        // Executando
//        Page<SaldoViewDTO> saldosResultados = consultaService.saldosPorUsuario(idUsuarioInformado,pageRequest);
//
//        // Verificando
//        assertThat(saldosResultados).isNotEmpty();
//        assertThat(saldosResultados.getTotalElements()).isEqualTo(4);
//        assertThat(saldosResultados.getTotalPages()).isEqualTo(2);
//        assertThat(saldosResultados.getSize()).isEqualTo(2);
//    }
//
//
//    @Test
//    @DisplayName("Saldos por usuário não deveria retornar uma lista quando o usuário logado não estiver na conta da adminstração.")
//    void naoDeveMostrarSaldosPorUsuario(){
//        // Cenário
//
//        // Recuperar Usuário logado como Cliente
//        when(perfilService.recuperarUsuarioLogado()).thenReturn(gerarUsuarioLogado(3L, ROLE_CLIENTE, 3L, "José Marques"));
//
//        // Verificar Usuário logado como Admin
//        when(perfilService.isContemPerfilClienteUsuarioLogado()).thenReturn(true);
//
//        // Usuário informado
//        Long idUsuarioInformado = 1L;
//
//        PageRequest pageRequest = PageRequest.of(0,2, Sort.Direction.DESC,"dataDeposito");
//        assertThrows(ExcecaoPersonalizada.class, () -> consultaService.saldosPorUsuario(idUsuarioInformado,pageRequest));
//    }
//
//    @Test
//    @DisplayName("Saques por usuário deveria retornar uma lista quando informar o usuário já cadastrado.")
//    void deveMostrarSaquesPorUsuario(){
//        // Cenário
//
//        // Recuperar Usuário logado como Admin
//        when(perfilService.recuperarUsuarioLogado()).thenReturn(gerarUsuarioLogado(1L, ROLE_ADMIN, 1L, "Gabriel Ferreira"));
//
//        // Verificar Usuário logado como Admin
//        when(perfilService.isContemPerfilAdminUsuarioLogado()).thenReturn(true);
//
//        Long idUsuarioInformado = 1L;
//
//        // Mock para retornar saques do usuário
//        PageRequest pageRequest = PageRequest.of(0,1, Sort.Direction.DESC,"dataSaque");
//        List<SaqueViewDTO> saqueViewDTOS = saquesCriados();
//        QSaque qSaque =  QSaque.saque;
//        when(queryDslDAO.query(any())).thenReturn(jpaQuerySaque);
//        when(jpaQuerySaque.from(qSaque)).thenReturn(jpaQuerySaque);
//        when(jpaQuerySaque.innerJoin(qSaque.usuario)).thenReturn(jpaQuerySaque);
//        when(jpaQuerySaque.where(any(Predicate.class))).thenReturn(jpaQuerySaque);
//        when(jpaQuerySaque.offset(pageRequest.getOffset())).thenReturn(jpaQuerySaque);
//        when(jpaQuerySaque.limit(pageRequest.getPageSize())).thenReturn(jpaQuerySaque);
//        when(jpaQuerySaque.orderBy(any(OrderSpecifier.class))).thenReturn(jpaQuerySaque);
//        when(jpaQuerySaque.fetch()).thenReturn(saqueViewDTOS);
//
//        // Executando
//        Page<SaqueViewDTO> saquesResultados = consultaService.saquesPorUsuario(idUsuarioInformado,pageRequest);
//
//        // Verificando
//        assertThat(saquesResultados).isNotEmpty();
//        assertThat(saquesResultados.getTotalElements()).isEqualTo(2);
//        assertThat(saquesResultados.getTotalPages()).isEqualTo(2);
//        assertThat(saquesResultados.getSize()).isEqualTo(1);
//    }
//
//    @Test
//    @DisplayName("Saques por usuário não deveria retornar uma lista quando o usuário logado não estiver na conta da adminstração.")
//    void naoDeveMostrarSaquesPorUsuario(){
//        // Cenário
//
//        // Recuperar Usuário logado como Cliente
//        when(perfilService.recuperarUsuarioLogado()).thenReturn(gerarUsuarioLogado(5L, ROLE_CLIENTE, 5L, "Marina Silva"));
//
//        // Verificar Usuário logado como Cliente
//        when(perfilService.isContemPerfilAdminUsuarioLogado()).thenReturn(false);
//
//        // Usuário informado
//        Long idUsuarioInformado = 1L;
//
//        PageRequest pageRequest = PageRequest.of(0,1, Sort.Direction.DESC,"dataSaque");
//        assertThrows(ExcecaoPersonalizada.class, () -> consultaService.saquesPorUsuario(idUsuarioInformado,pageRequest));
//    }
//
//
//    @Test
//    @DisplayName("Deve retornar lista de usuários quando tiver dados no banco de dados.")
//    void deveRetornarUsuariosPaginados(){
//        // Cenário
//
//        List<Usuario> usuarios = criarUsuarios();
//
//        // Mock para retornar os dados de cima
//        PageRequest pageRequest = PageRequest.of(0,2, Sort.Direction.DESC,"nome");
//
//
//        // Mock
//        when(usuarioRepositorio.buscarUsuarios(pageRequest)).thenReturn(toPages(usuarios, pageRequest));
//
//        // Execução
//        Page<UsuarioViewDTO> usuarioPage = consultaService.listagem(pageRequest);
//
//        // Verificação
//        assertThat(usuarioPage.getTotalElements()).isEqualTo(3);
//        assertThat(usuarioPage.getTotalPages()).isEqualTo(2);
//        assertThat(usuarioPage.isEmpty()).isFalse();
//        assertThat(usuarioPage.getContent().get(0).perfis().get(0).getDescricao()).isEqualTo("Admin");
//    }
//
//    @Test
//    @DisplayName("Não deve retornar lista de usuários quando tiver com cpf formato incorreto")
//    void naoDeveRetornarUsuariosPaginados(){
//        // Cenário
//
//        List<Usuario> usuarios = criarUsuarios();
//        usuarios.get(0).setCpf("12---12222----2");
//
//        // Mock para retornar os dados de cima
//        PageRequest pageRequest = PageRequest.of(0,2, Sort.Direction.DESC,"nome");
//
//        // Mock
//        when(usuarioRepositorio.buscarUsuarios(pageRequest)).thenReturn(toPages(usuarios, pageRequest));
//
//        // Executando e verificando
//        assertThrows(Exception.class, () -> consultaService.listagem(pageRequest));
//    }
//
//
//    private List<Usuario> criarUsuarios(){
//        List<Usuario> usuarios = new ArrayList<>();
//        Perfil perfil = Perfil.builder().id(1L).nome("Admin").build();
//        usuarios.add(new Usuario(1L,"Teste","teste@email.com",null,null, null, null, null, null, List.of(perfil)));
//        usuarios.add(new Usuario(2L,"Teste 2","teste22@email.com",null,null, null, null, null, null, List.of(perfil)));
//        usuarios.add(new Usuario(3L,"Teste 3","teste33@email.com",null,null, null, null, null, null, List.of(perfil)));
//        return usuarios;
//    }
//
//    private Page<Usuario> toPages(List<Usuario> usuarios, PageRequest pageable){
//        int inicio = pageable.getPageNumber() * pageable.getPageSize();
//        if (inicio > usuarios.size()) {
//            return new PageImpl<>(new ArrayList<>(), pageable, 0);
//        }
//
//        int fim = Math.min(inicio + pageable.getPageSize(), usuarios.size());
//        return new PageImpl<>(usuarios.subList(inicio, fim), pageable, usuarios.size());
//    }
//
//    private List<SaldoViewDTO> saldosCriados() {
//        List<SaldoViewDTO> saldos = new ArrayList<>();
//        saldos.add(new SaldoViewDTO(1L,BigDecimal.valueOf(200.00),LocalDateTime.parse("30/06/2022 13:00:00", DTFHORA)));
//        saldos.add(new SaldoViewDTO(2L,BigDecimal.valueOf(500.00),LocalDateTime.parse("30/06/2022 13:00:00", DTFHORA)));
//        saldos.add(new SaldoViewDTO(3L,BigDecimal.valueOf(600.00),LocalDateTime.parse("30/06/2022 13:00:00", DTFHORA)));
//        saldos.add(new SaldoViewDTO(4L,BigDecimal.valueOf(800.00),LocalDateTime.parse("30/06/2022 13:00:00", DTFHORA)));
//        return saldos;
//    }
//
//    private List<SaqueViewDTO> saquesCriados(){
//        List<SaqueViewDTO> saques = new ArrayList<>();
//        saques.add(new SaqueViewDTO(1L,LocalDateTime.now(),BigDecimal.valueOf(250.00)));
//        saques.add(new SaqueViewDTO(2L,LocalDateTime.now(),BigDecimal.valueOf(550.00)));
//        return saques;
//    }
//
//}
