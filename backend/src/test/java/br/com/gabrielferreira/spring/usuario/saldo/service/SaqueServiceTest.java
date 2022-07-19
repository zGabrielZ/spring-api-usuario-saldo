//package br.com.gabrielferreira.spring.usuario.saldo.service;
//
//import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saldo;
//import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saque;
//import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
//import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.SacarFormDTO;
//import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
//import br.com.gabrielferreira.spring.usuario.saldo.repositorio.SaqueRepositorio;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(SpringExtension.class)
//@ActiveProfiles("test")
//class SaqueServiceTest {
//
//    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
//    private SaqueRepositorio saqueRepositorio;
//    private UsuarioService usuarioService;
//    private SaqueService saqueService;
//
//    @BeforeEach
//    void criarInstancias(){
//        saqueRepositorio = mock(SaqueRepositorio.class);
//        usuarioService = mock(UsuarioService.class);
//        saqueService = new SaqueService(saqueRepositorio,usuarioService);
//    }
//
//    @Test
//    @DisplayName("Deve sacar valor quando tiver valores na conta do usuário.")
//    void deveSacarValor(){
//        // Cenário
//        Long usuarioId = 1L;
//        Usuario usuario = usuarioCriado();
//        usuario.setSaldoTotal(BigDecimal.valueOf(200.00));
//
//        // Mock para retornar o usuário
//        when(usuarioService.buscarPorId(usuarioId)).thenReturn(usuario);
//
//        // Executando
//        SacarFormDTO sacarFormDTO = criarSaqueFormDto(BigDecimal.valueOf(50.00),usuarioId);
//        BigDecimal valorTotal = saqueService.sacar(sacarFormDTO);
//
//        // Verificando
//        assertThat(valorTotal).isEqualTo(BigDecimal.valueOf(150.00));
//        verify(saqueRepositorio).save(any());
//    }
//
//    @Test
//    @DisplayName("Não deveria sacar valor quando tiver com o saldo 0 ou nulo.")
//    void naoDeveSacarValorQuandoNaoTiverSaldo(){
//        // Cenário
//        Long usuarioId = 1L;
//        Usuario usuario = usuarioCriado();
//
//        // Mock para retornar o usuário
//        when(usuarioService.buscarPorId(usuarioId)).thenReturn(usuario);
//
//        // Executando e verificando
//        SacarFormDTO sacarFormDTO = criarSaqueFormDto(BigDecimal.valueOf(500.00),usuarioId);
//        assertThrows(ExcecaoPersonalizada.class, () -> saqueService.sacar(sacarFormDTO));
//        verify(saqueRepositorio,never()).save(any());
//    }
//
//    @Test
//    @DisplayName("Não deveria sacar valor quando o saldo total for menor do que o saque.")
//    void naoDeveSacarValorQuandoForMaiorDoQueSaldoTotal(){
//        // Cenário
//        Long usuarioId = 1L;
//        Usuario usuario = usuarioCriado();
//        usuario.setSaldoTotal(BigDecimal.valueOf(200.00));
//
//        // Mock para retornar o usuário
//        when(usuarioService.buscarPorId(usuarioId)).thenReturn(usuario);
//
//        // Executando e verificando
//        SacarFormDTO sacarFormDTO = criarSaqueFormDto(BigDecimal.valueOf(500.00),usuarioId);
//        assertThrows(ExcecaoPersonalizada.class, () -> saqueService.sacar(sacarFormDTO));
//        verify(saqueRepositorio,never()).save(any());
//    }
//
//    @Test
//    @DisplayName("Saques por usuário deveria retornar uma lista quando informar o usuário já cadastrado.")
//    void deveMostrarSaquesPorUsuario(){
//        // Cenário
//        Long idUsuarioPesquisar = 1L;
//        // Mock para retornar um usuário
//        Usuario usuario = usuarioCriado();
//        when(usuarioService.buscarPorId(idUsuarioPesquisar)).thenReturn(usuario);
//        // Mock para retornar saldos do usuários
//        List<Saque> saques = saqueCriados(usuario);
//        PageRequest pageRequest = PageRequest.of(0,1, Sort.Direction.DESC,"dataSaque");
//        when(saqueRepositorio.buscarPorUsuario(idUsuarioPesquisar,pageRequest)).thenReturn(saques);
//
//        // Executando
//        Page<Saque> saquesResultados = saqueService.saquesPorUsuario(idUsuarioPesquisar,pageRequest);
//
//        // Verificando
//        assertThat(saquesResultados).isNotEmpty();
//        assertThat(saquesResultados.getTotalElements()).isEqualTo(2);
//        assertThat(saquesResultados.getTotalPages()).isEqualTo(2);
//        assertThat(saquesResultados.getSize()).isEqualTo(1);
//    }
//
//    @Test
//    @DisplayName("Saques por usuário deveria retornar uma lista vazia quando não tiver saques associado ao usuário pesquisado.")
//    void deveMostrarSaquesVazioPorUsuarioNaoEncontrado(){
//        // Cenário
//        Long idUsuarioPesquisar = 1L;
//
//        // Mock para retornar usuario quando informar o id
//        Usuario usuario = usuarioCriado();
//        when(usuarioService.buscarPorId(idUsuarioPesquisar)).thenReturn(usuario);
//
//        // Executando e verificando
//        PageRequest pageRequest = PageRequest.of(0,1, Sort.Direction.DESC,"dataSaque");
//        Page<Saque> saquesResultados = saqueService.saquesPorUsuario(idUsuarioPesquisar,pageRequest);
//
//        // Verificando
//        assertThat(saquesResultados).isEmpty();
//    }
//
//    private SacarFormDTO criarSaqueFormDto(BigDecimal quantidade, Long idUsuario){
//        return SacarFormDTO.builder().quantidade(quantidade).idUsuario(idUsuario).build();
//    }
//
//    private Usuario usuarioCriado(){
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        return Usuario.builder().id(1L).nome("José Ferreira").email("jose@gmail.com")
//                .senha("$2a$10$rkFB6IzKB9M/T8UBxe11eOS0dsUJxxe0.R2OLhkMqFtfHdOqypwZS").cpf("73977674005")
//                .dataNascimento(LocalDate.parse("10/02/1990",dateTimeFormatter))
//                .saldos(new ArrayList<>()).saques(new ArrayList<>())
//                .build();
//    }
//
//    private List<Saldo> saldosCriados(Usuario usuario){
//        List<Saldo> saldos = new ArrayList<>();
//        saldos.add(Saldo.builder().id(1L).deposito(BigDecimal.valueOf(200.00))
//                .dataDeposito(LocalDateTime.parse("30/06/2022 13:00:00",dateTimeFormatter)).usuario(usuario).build());
//        saldos.add(Saldo.builder().id(2L).deposito(BigDecimal.valueOf(500.00))
//                .dataDeposito(LocalDateTime.parse("30/06/2022 13:00:00",dateTimeFormatter)).usuario(usuario).build());
//        saldos.add(Saldo.builder().id(3L).deposito(BigDecimal.valueOf(600.00))
//                .dataDeposito(LocalDateTime.parse("30/06/2022 13:00:00",dateTimeFormatter)).usuario(usuario).build());
//        saldos.add(Saldo.builder().id(4L).deposito(BigDecimal.valueOf(800.00))
//                .dataDeposito(LocalDateTime.parse("30/06/2022 13:00:00",dateTimeFormatter)).usuario(usuario).build());
//        return saldos;
//    }
//
//    private List<Saque> saqueCriados(Usuario usuario){
//        List<Saque> saques = new ArrayList<>();
//        saques.add(Saque.builder().id(1L).valor(BigDecimal.valueOf(250.00)).usuario(usuario).build());
//        saques.add(Saque.builder().id(2L).valor(BigDecimal.valueOf(550.00)).usuario(usuario).build());
//        return saques;
//    }
//
//    private Page<Saque> listParaPage(List<Saque> saques, PageRequest pageRequest){
//        return new PageImpl<>(saques,pageRequest,saques.size());
//    }
//}
