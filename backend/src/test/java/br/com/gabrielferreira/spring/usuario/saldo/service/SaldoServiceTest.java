//package br.com.gabrielferreira.spring.usuario.saldo.service;
//import br.com.gabrielferreira.spring.usuario.saldo.client.FeriadoNacionalClient;
//import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.FeriadoNacionalDTO;
//import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoViewDTO;
//import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioViewDTO;
//import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saldo;
//import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoInsertFormDTO;
//import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saque;
//import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
//import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
//import br.com.gabrielferreira.spring.usuario.saldo.repositorio.SaldoRepositorio;
//import static org.assertj.core.api.Assertions.*;
//import br.com.gabrielferreira.spring.usuario.saldo.repositorio.SaqueRepositorio;
//import br.com.gabrielferreira.spring.usuario.saldo.controller.AbstractController;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import java.math.BigDecimal;
//import java.time.Clock;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//@ExtendWith(SpringExtension.class)
//class SaldoServiceTest extends AbstractController {
//
//    private Clock clock;
//
//    private PerfilService perfilService;
//
//    private FeriadoNacionalClient feriadoNacionalClient;
//
//    private UsuarioService usuarioService;
//
//    private SaldoRepositorio saldoRepositorio;
//
//    private SaqueRepositorio saqueRepositorio;
//
//    private SaldoService saldoService;
//
//    @BeforeEach
//    void criarInstancias(){
//        usuarioService = mock(UsuarioService.class);
//        perfilService = mock(PerfilService.class);
//        saldoRepositorio = mock(SaldoRepositorio.class);
//        saqueRepositorio = mock(SaqueRepositorio.class);
//        feriadoNacionalClient = mock(FeriadoNacionalClient.class);
//        clock = mock(Clock.class);
//        saldoService = new SaldoService(clock,perfilService,feriadoNacionalClient,usuarioService,saldoRepositorio,saqueRepositorio);
//    }
//
//    @Test
//    @DisplayName("Depositar um saldo por usuário deveria salvar no banco de dados quando informar informações corretas.")
//    void deveDepositar(){
//        // Cenário
//
//        Long idUsuarioInformado = 10L;
//        // Mock para retornar um usuário qualquer
//        UsuarioViewDTO usuarioViewDTO = new UsuarioViewDTO(idUsuarioInformado, null,null,null,null,null);
//        when(usuarioService.buscarPorId(idUsuarioInformado)).thenReturn(usuarioViewDTO);
//
//        // Recuperar Usuário logado como Admin
//        when(perfilService.recuperarUsuarioLogado()).thenReturn(gerarUsuarioLogado(1L, ROLE_ADMIN, 1L, "Marcos da Silva"));
//
//        // Mock para retornar um saldo quando tiver salvo
//        SaldoInsertFormDTO saldoFormDTO = SaldoInsertFormDTO.builder()
//                .deposito(BigDecimal.valueOf(500.00))
//                .idUsuario(idUsuarioInformado).build();
//
//        Saldo saldoJaSalvo = Saldo.builder().id(123L).usuario(Usuario.builder().id(usuarioViewDTO.id()).build())
//                .dataDeposito(LocalDateTime.parse("30/06/2022 13:00:00",DTFHORA))
//                .deposito(BigDecimal.valueOf(500.00)).build();
//
//        // Mock para data atual fixa
//        LocalDateTime dataAtualFixa = LocalDateTime.parse("06/10/2022 10:00:00", DTFHORA);
//        when(clock.instant()).thenReturn(dataAtualFixa.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
//        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
//
//        saldoJaSalvo.setDataDeposito(dataAtualFixa);
//        when(saldoRepositorio.save(any())).thenReturn(saldoJaSalvo);
//
//
//        // Executando
//        SaldoViewDTO saldoResultado = saldoService.depositar(saldoFormDTO);
//
//        // Verificando
//        assertThat(saldoResultado.id()).isNotNull();
//        assertThat(saldoResultado.deposito()).isEqualTo(BigDecimal.valueOf(500.00));
//        assertThat(saldoResultado.dataDeposito()).isEqualTo(LocalDateTime.parse("06/10/2022 10:00:00",DTFHORA));
//    }
//
//    @Test
//    @DisplayName("Depositar saldo não deveria salvar quando a quantidade for menor do que 0.")
//    void naoDeveDepositarSaldoMenorQueZero(){
//        // Cenário
//
//        Long idUsuarioInformado = 10L;
//        // Mock para retornar um usuário qualquer
//        UsuarioViewDTO usuarioViewDTO = new UsuarioViewDTO(idUsuarioInformado, null,null,null,null,null);
//        when(usuarioService.buscarPorId(idUsuarioInformado)).thenReturn(usuarioViewDTO);
//
//        // Recuperar Usuário logado como Admin
//        when(perfilService.recuperarUsuarioLogado()).thenReturn(gerarUsuarioLogado(1L, ROLE_ADMIN, 40L, "José da Silva"));
//
//        SaldoInsertFormDTO saldoFormDTO = SaldoInsertFormDTO.builder().deposito(BigDecimal.valueOf(-500.00))
//                .idUsuario(idUsuarioInformado).build();
//
//        // Executando e verificando
//        assertThrows(ExcecaoPersonalizada.class, () -> saldoService.depositar(saldoFormDTO));
//        verify(saldoRepositorio,never()).save(any());
//    }
//
//    @Test
//    @DisplayName("Depositar saldo não deveria salvar quando a quantidade for igual a 0.")
//    void naoDeveDepositarSaldoIgualZero(){
//        // Cenário
//
//        Long idUsuarioInformado = 10L;
//        // Mock para retornar um usuário qualquer
//        UsuarioViewDTO usuarioViewDTO = new UsuarioViewDTO(idUsuarioInformado, null,null,null,null,null);
//        when(usuarioService.buscarPorId(idUsuarioInformado)).thenReturn(usuarioViewDTO);
//
//        // Recuperar Usuário logado como Admin
//        when(perfilService.recuperarUsuarioLogado()).thenReturn(gerarUsuarioLogado(1L, ROLE_ADMIN, 20L, "Gabriel da Silva"));
//
//        SaldoInsertFormDTO saldoFormDTO = SaldoInsertFormDTO.builder().deposito(BigDecimal.valueOf(0.0))
//                .idUsuario(idUsuarioInformado).build();
//
//        // Executando e verificando
//        assertThrows(ExcecaoPersonalizada.class, () -> saldoService.depositar(saldoFormDTO));
//        verify(saldoRepositorio,never()).save(any());
//    }
//
//    @Test
//    @DisplayName("Depositar saldo não deveria salvar quando for final de semana.")
//    void naoDeveDepositarSaldoFinalDeSemana(){
//        // Cenário
//
//        Long idUsuarioInformado = 10L;
//        // Mock para retornar um usuário qualquer
//        UsuarioViewDTO usuarioViewDTO = new UsuarioViewDTO(idUsuarioInformado, null,null,null,null,null);
//        when(usuarioService.buscarPorId(idUsuarioInformado)).thenReturn(usuarioViewDTO);
//
//        // Recuperar Usuário logado como Admin
//        when(perfilService.recuperarUsuarioLogado()).thenReturn(gerarUsuarioLogado(1L, ROLE_ADMIN, 4L, "Thiago da Silva"));
//
//        SaldoInsertFormDTO saldoFormDTO = SaldoInsertFormDTO.builder().deposito(BigDecimal.valueOf(100.0))
//                .idUsuario(idUsuarioInformado).build();
//
//        // Mock para data atual fixa
//        LocalDateTime dataAtualFixa = LocalDateTime.parse("23/07/2022 10:00:00", DTFHORA);
//        when(clock.instant()).thenReturn(dataAtualFixa.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
//        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
//
//
//        // Executando e verificando
//        assertThrows(ExcecaoPersonalizada.class, () -> saldoService.depositar(saldoFormDTO));
//        verify(saldoRepositorio,never()).save(any());
//    }
////
//    @Test
//    @DisplayName("Depositar saldo não deveria salvar quando for feriado nacional.")
//    void naoDeveDepositarSaldoFeriadoNacional(){
//        // Cenário
//
//        Long idUsuarioInformado = 10L;
//        // Mock para retornar um usuário qualquer
//        UsuarioViewDTO usuarioViewDTO = new UsuarioViewDTO(idUsuarioInformado, null,null,null,null,null);
//        when(usuarioService.buscarPorId(idUsuarioInformado)).thenReturn(usuarioViewDTO);
//
//        // Recuperar Usuário logado como Admin
//        when(perfilService.recuperarUsuarioLogado()).thenReturn(gerarUsuarioLogado(1L, ROLE_ADMIN, 11L, "Maria Souza"));
//
//        SaldoInsertFormDTO saldoFormDTO = SaldoInsertFormDTO.builder().deposito(BigDecimal.valueOf(100.0))
//                .idUsuario(idUsuarioInformado).build();
//
//        // Mock para data atual fixa
//        LocalDateTime dataAtualFixa = LocalDateTime.parse("07/10/2022 10:00:00", DTFHORA);
//        when(clock.instant()).thenReturn(dataAtualFixa.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
//        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
//
//        // Mock para feriado nacional
//        FeriadoNacionalDTO feriadoNacionalDTO = FeriadoNacionalDTO.builder().date(dataAtualFixa.toLocalDate()).build();
//        when(feriadoNacionalClient.buscarFeriadosNacionais(2022)).thenReturn(Collections.singletonList(feriadoNacionalDTO));
//
//        // Executando e verificando
//        assertThrows(ExcecaoPersonalizada.class, () -> saldoService.depositar(saldoFormDTO));
//        verify(saldoRepositorio,never()).save(any());
//    }
//
//    @Test
//    @DisplayName("Saldo total por usuário deveria retornar um valor quando informar um usuário.")
//    void deveMostrarSaldoTotalPorUsuario(){
//        // Cenário
//
//        Long idUsuarioInformado = 10L;
//        // Mock para retornar um usuário qualquer
//        UsuarioViewDTO usuarioViewDTO = new UsuarioViewDTO(idUsuarioInformado, null,null,null,null,null);
//        when(usuarioService.buscarPorId(idUsuarioInformado)).thenReturn(usuarioViewDTO);
//
//        // Recuperar Usuário logado como Admin
//        when(perfilService.recuperarUsuarioLogado()).thenReturn(gerarUsuarioLogado(1L, ROLE_ADMIN, 50L, "José"));
//
//        // Mock para retornar saldos por usuário
//        List<Saldo> saldos = saldosCriados();
//        when(saldoRepositorio.findByUsuarioId(anyLong())).thenReturn(saldos);
//
//        // Mock para retornar saques vazios por usuário
//        List<Saque> saques = new ArrayList<>();
//        when(saqueRepositorio.findByUsuarioId(anyLong())).thenReturn(saques);
//
//        // Executando
//        BigDecimal valorTotal = saldoService.saldoTotalPorUsuario(idUsuarioInformado);
//
//        // Verificando
//        assertThat(valorTotal).isEqualTo(BigDecimal.valueOf(2100.00));
//
//    }
//
//    @Test
//    @DisplayName("Saldo total por usuário deveria retornar um valor quando informar um usuário que já tem seus saques registrados.")
//    void deveMostrarSaldoTotalPorUsuarioQuandoTiverSaque(){
//        // Cenário
//
//        Long idUsuarioInformado = 10L;
//        // Mock para retornar um usuário qualquer
//        UsuarioViewDTO usuarioViewDTO = new UsuarioViewDTO(idUsuarioInformado, null,null,null,null,null);
//        when(usuarioService.buscarPorId(idUsuarioInformado)).thenReturn(usuarioViewDTO);
//
//        // Recuperar Usuário logado como Admin
//        when(perfilService.recuperarUsuarioLogado()).thenReturn(gerarUsuarioLogado(1L, ROLE_ADMIN, 3L, "Mariana"));
//
//        // Mock para retornar saldos por usuário
//        List<Saldo> saldos = saldosCriados();
//        when(saldoRepositorio.findByUsuarioId(anyLong())).thenReturn(saldos);
//
//        // Mock para retornar saques vazios por usuário
//        List<Saque> saques = saquesCriados();
//        when(saqueRepositorio.findByUsuarioId(anyLong())).thenReturn(saques);
//
//        // Executando
//        BigDecimal valorTotal = saldoService.saldoTotalPorUsuario(idUsuarioInformado);
//
//        // Verificando
//        assertThat(valorTotal).isEqualTo(BigDecimal.valueOf(1300.00));
//
//    }
//
//    @Test
//    @DisplayName("Depositar saldo não deveria salvar quando tiver com a mesma conta.")
//    void naoDeveDepositarSaldoContaPropria(){
//        // Cenário
//
//        Long idUsuarioInformado = 1L;
//        // Mock para retornar um usuário qualquer
//        UsuarioViewDTO usuarioViewDTO = new UsuarioViewDTO(idUsuarioInformado, null,null,null,null,null);
//        when(usuarioService.buscarPorId(idUsuarioInformado)).thenReturn(usuarioViewDTO);
//
//        // Recuperar Usuário logado como Admin
//        when(perfilService.recuperarUsuarioLogado()).thenReturn(gerarUsuarioLogado(1L, ROLE_ADMIN, 1L, "Josué"));
//
//        SaldoInsertFormDTO saldoFormDTO = SaldoInsertFormDTO.builder().deposito(BigDecimal.valueOf(-500.00))
//                .idUsuario(idUsuarioInformado).build();
//
//        // Executando e verificando
//        assertThrows(ExcecaoPersonalizada.class, () -> saldoService.depositar(saldoFormDTO));
//        verify(saldoRepositorio,never()).save(any());
//    }
//
//    private List<Saldo> saldosCriados(){
//        List<Saldo> saldos = new ArrayList<>();
//        saldos.add(Saldo.builder().id(1L).deposito(BigDecimal.valueOf(200.00))
//                .dataDeposito(LocalDateTime.parse("30/06/2022 13:00:00",DTFHORA)).build());
//        saldos.add(Saldo.builder().id(2L).deposito(BigDecimal.valueOf(500.00))
//                .dataDeposito(LocalDateTime.parse("30/06/2022 13:00:00",DTFHORA)).build());
//        saldos.add(Saldo.builder().id(3L).deposito(BigDecimal.valueOf(600.00))
//                .dataDeposito(LocalDateTime.parse("30/06/2022 13:00:00",DTFHORA)).build());
//        saldos.add(Saldo.builder().id(4L).deposito(BigDecimal.valueOf(800.00))
//                .dataDeposito(LocalDateTime.parse("30/06/2022 13:00:00",DTFHORA)).build());
//        return saldos;
//    }
//
//    private List<Saque> saquesCriados(){
//        List<Saque> saques = new ArrayList<>();
//        saques.add(Saque.builder().id(1L).valor(BigDecimal.valueOf(250.00)).build());
//        saques.add(Saque.builder().id(2L).valor(BigDecimal.valueOf(550.00)).build());
//        return saques;
//    }
//}
