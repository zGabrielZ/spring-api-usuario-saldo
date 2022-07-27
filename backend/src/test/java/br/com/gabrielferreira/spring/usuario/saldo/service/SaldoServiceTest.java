package br.com.gabrielferreira.spring.usuario.saldo.service;
import br.com.gabrielferreira.spring.usuario.saldo.client.FeriadoNacionalClient;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.FeriadoNacionalDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saldo;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saque;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.SaldoRepositorio;
import static org.assertj.core.api.Assertions.*;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.SaqueRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
class SaldoServiceTest {

    private static final DateTimeFormatter DTFHORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DateTimeFormatter DTFDIA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Clock clock;

    private FeriadoNacionalClient feriadoNacionalClient;

    private UsuarioService usuarioService;

    private SaldoRepositorio saldoRepositorio;

    private SaqueRepositorio saqueRepositorio;

    private SaldoService saldoService;

    @BeforeEach
    void criarInstancias(){
        usuarioService = mock(UsuarioService.class);
        saldoRepositorio = mock(SaldoRepositorio.class);
        saqueRepositorio = mock(SaqueRepositorio.class);
        feriadoNacionalClient = mock(FeriadoNacionalClient.class);
        clock = mock(Clock.class);
        saldoService = new SaldoService(clock,feriadoNacionalClient,usuarioService,saldoRepositorio,saqueRepositorio);
    }

    @Test
    @DisplayName("Depositar um saldo por usuário deveria salvar no banco de dados quando informar informações corretas.")
    void deveDepositar(){
        // Cenário

        Long idUsuarioInformado = 1L;
        // Mock para retornar um usuário qualquer
        UsuarioViewDTO usuarioViewDTO = UsuarioViewDTO.builder().id(idUsuarioInformado).build();
        when(usuarioService.buscarPorId(idUsuarioInformado)).thenReturn(usuarioViewDTO);

        // Mock para retornar um saldo quando tiver salvo
        SaldoFormDTO saldoFormDTO = SaldoFormDTO.builder()
                .deposito(BigDecimal.valueOf(500.00))
                .idUsuario(idUsuarioInformado).build();

        Saldo saldoJaSalvo = Saldo.builder().id(123L).usuario(Usuario.builder().id(usuarioViewDTO.getId()).build()).dataDeposito(LocalDateTime.parse("30/06/2022 13:00:00",DTFHORA))
                .deposito(BigDecimal.valueOf(500.00)).build();

        // Mock para data atual fixa
        LocalDateTime dataAtualFixa = LocalDateTime.parse("06/10/2022 10:00:00", DTFHORA);
        when(clock.instant()).thenReturn(dataAtualFixa.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        saldoJaSalvo.setDataDeposito(dataAtualFixa);
        when(saldoRepositorio.save(any())).thenReturn(saldoJaSalvo);


        // Executando
        SaldoViewDTO saldoResultado = saldoService.depositar(saldoFormDTO);

        // Verificando
        assertThat(saldoResultado.getId()).isNotNull();
        assertThat(saldoResultado.getDeposito()).isEqualTo(BigDecimal.valueOf(500.00));
        assertThat(saldoResultado.getDataDeposito()).isEqualTo(LocalDateTime.parse("06/10/2022 10:00:00",DTFHORA));
    }

    @Test
    @DisplayName("Depositar saldo não deveria salvar quando a quantidade for menor do que 0.")
    void naoDeveDepositarSaldoMenorQueZero(){
        // Cenário

        Long idUsuarioInformado = 1L;
        // Mock para retornar um usuário qualquer
        UsuarioViewDTO usuarioViewDTO = UsuarioViewDTO.builder().id(idUsuarioInformado).build();
        when(usuarioService.buscarPorId(idUsuarioInformado)).thenReturn(usuarioViewDTO);

        SaldoFormDTO saldoFormDTO = SaldoFormDTO.builder().deposito(BigDecimal.valueOf(-500.00))
                .idUsuario(idUsuarioInformado).build();

        // Executando e verificando
        assertThrows(ExcecaoPersonalizada.class, () -> saldoService.depositar(saldoFormDTO));
        verify(saldoRepositorio,never()).save(any());
    }

    @Test
    @DisplayName("Depositar saldo não deveria salvar quando a quantidade for igual a 0.")
    void naoDeveDepositarSaldoIgualZero(){
        // Cenário

        Long idUsuarioInformado = 1L;
        // Mock para retornar um usuário qualquer
        UsuarioViewDTO usuarioViewDTO = UsuarioViewDTO.builder().id(idUsuarioInformado).build();
        when(usuarioService.buscarPorId(idUsuarioInformado)).thenReturn(usuarioViewDTO);

        SaldoFormDTO saldoFormDTO = SaldoFormDTO.builder().deposito(BigDecimal.valueOf(0.0))
                .idUsuario(idUsuarioInformado).build();

        // Executando e verificando
        assertThrows(ExcecaoPersonalizada.class, () -> saldoService.depositar(saldoFormDTO));
        verify(saldoRepositorio,never()).save(any());
    }

    @Test
    @DisplayName("Depositar saldo não deveria salvar quando for final de semana.")
    void naoDeveDepositarSaldoFinalDeSemana(){
        // Cenário

        Long idUsuarioInformado = 1L;
        // Mock para retornar um usuário qualquer
        UsuarioViewDTO usuarioViewDTO = UsuarioViewDTO.builder().id(idUsuarioInformado).build();
        when(usuarioService.buscarPorId(idUsuarioInformado)).thenReturn(usuarioViewDTO);

        SaldoFormDTO saldoFormDTO = SaldoFormDTO.builder().deposito(BigDecimal.valueOf(100.0))
                .idUsuario(idUsuarioInformado).build();

        // Mock para data atual fixa
        LocalDateTime dataAtualFixa = LocalDateTime.parse("23/07/2022 10:00:00", DTFHORA);
        when(clock.instant()).thenReturn(dataAtualFixa.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());


        // Executando e verificando
        assertThrows(ExcecaoPersonalizada.class, () -> saldoService.depositar(saldoFormDTO));
        verify(saldoRepositorio,never()).save(any());
    }

    @Test
    @DisplayName("Depositar saldo não deveria salvar quando for feriado nacional.")
    void naoDeveDepositarSaldoFeriadoNacional(){
        // Cenário

        Long idUsuarioInformado = 1L;
        // Mock para retornar um usuário qualquer
        UsuarioViewDTO usuarioViewDTO = UsuarioViewDTO.builder().id(idUsuarioInformado).build();
        when(usuarioService.buscarPorId(idUsuarioInformado)).thenReturn(usuarioViewDTO);

        SaldoFormDTO saldoFormDTO = SaldoFormDTO.builder().deposito(BigDecimal.valueOf(100.0))
                .idUsuario(idUsuarioInformado).build();

        // Mock para data atual fixa
        LocalDateTime dataAtualFixa = LocalDateTime.parse("07/10/2022 10:00:00", DTFHORA);
        when(clock.instant()).thenReturn(dataAtualFixa.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        // Mock para feriado nacional
        FeriadoNacionalDTO feriadoNacionalDTO = FeriadoNacionalDTO.builder().date(dataAtualFixa.toLocalDate()).build();
        when(feriadoNacionalClient.buscarFeriadosNacionais()).thenReturn(Arrays.asList(feriadoNacionalDTO));

        // Executando e verificando
        assertThrows(ExcecaoPersonalizada.class, () -> saldoService.depositar(saldoFormDTO));
        verify(saldoRepositorio,never()).save(any());
    }

    @Test
    @DisplayName("Saldos por usuário deveria retornar uma lista quando informar o usuário já cadastrado.")
    void deveMostrarSaldosPorUsuario(){
        // Cenário

        Long idUsuarioInformado = 1L;
        // Mock para retornar um usuário qualquer
        UsuarioViewDTO usuarioViewDTO = UsuarioViewDTO.builder().id(idUsuarioInformado).build();
        when(usuarioService.buscarPorId(idUsuarioInformado)).thenReturn(usuarioViewDTO);

        // Mock para retornar os saldos por usuário
        PageRequest pageRequest = PageRequest.of(0,2, Sort.Direction.DESC,"dataDeposito");
        Page<Saldo> saldos = listSaldoParaPage(saldosCriados(), pageRequest);
        when(saldoRepositorio.buscarPorUsuario(usuarioViewDTO.getId(),pageRequest)).thenReturn(saldos);

        // Executando
        Page<SaldoViewDTO> saldosResultados = saldoService.saldosPorUsuario(idUsuarioInformado,pageRequest);

        // Verificando
        assertThat(saldosResultados).isNotEmpty();
        assertThat(saldosResultados.getTotalElements()).isEqualTo(4);
        assertThat(saldosResultados.getTotalPages()).isEqualTo(2);
        assertThat(saldosResultados.getSize()).isEqualTo(2);
    }

    @Test
    @DisplayName("Saldos por usuário deveria retornar uma lista vazia quando não tiver saldos associado ao usuário pesquisado.")
    void deveMostrarSaldosVazioPorUsuario(){
        // Cenário

        Long idUsuarioInformado = 1L;
        // Mock para retornar um usuário qualquer
        UsuarioViewDTO usuarioViewDTO = UsuarioViewDTO.builder().id(idUsuarioInformado).build();
        when(usuarioService.buscarPorId(idUsuarioInformado)).thenReturn(usuarioViewDTO);

        // Mock para retornar os saldos vazios por usuário
        PageRequest pageRequest = PageRequest.of(0,2, Sort.Direction.DESC,"dataDeposito");
        Page<Saldo> saldos = listSaldoParaPage(new ArrayList<>(), pageRequest);
        when(saldoRepositorio.buscarPorUsuario(usuarioViewDTO.getId(),pageRequest)).thenReturn(saldos);

        // Executando e verificando
        Page<SaldoViewDTO> saldosResultados = saldoService.saldosPorUsuario(idUsuarioInformado,pageRequest);

        // Verificando
        assertThat(saldosResultados).isEmpty();
    }

    @Test
    @DisplayName("Saldo total por usuário deveria retornar um valor quando informar um usuário.")
    void deveMostrarSaldoTotalPorUsuario(){
        // Cenário

        Long idUsuarioInformado = 1L;
        // Mock para retornar um usuário qualquer
        UsuarioViewDTO usuarioViewDTO = UsuarioViewDTO.builder().id(idUsuarioInformado).build();
        when(usuarioService.buscarPorId(idUsuarioInformado)).thenReturn(usuarioViewDTO);

        // Mock para retornar saldos por usuário
        List<Saldo> saldos = saldosCriados();
        when(saldoRepositorio.findByUsuarioId(anyLong())).thenReturn(saldos);

        // Mock para retornar saques vazios por usuário
        List<Saque> saques = new ArrayList<>();
        when(saqueRepositorio.findByUsuarioId(anyLong())).thenReturn(saques);

        // Executando
        BigDecimal valorTotal = saldoService.saldoTotalPorUsuario(idUsuarioInformado);

        // Verificando
        assertThat(valorTotal).isEqualTo(BigDecimal.valueOf(2100.00));

    }

    @Test
    @DisplayName("Saldo total por usuário deveria retornar um valor quando informar um usuário que já tem seus saques registrados.")
    void deveMostrarSaldoTotalPorUsuarioQuandoTiverSaque(){
        // Cenário

        Long idUsuarioInformado = 1L;
        // Mock para retornar um usuário qualquer
        UsuarioViewDTO usuarioViewDTO = UsuarioViewDTO.builder().id(idUsuarioInformado).build();
        when(usuarioService.buscarPorId(idUsuarioInformado)).thenReturn(usuarioViewDTO);

        // Mock para retornar saldos por usuário
        List<Saldo> saldos = saldosCriados();
        when(saldoRepositorio.findByUsuarioId(anyLong())).thenReturn(saldos);

        // Mock para retornar saques vazios por usuário
        List<Saque> saques = saquesCriados();
        when(saqueRepositorio.findByUsuarioId(anyLong())).thenReturn(saques);

        // Executando
        BigDecimal valorTotal = saldoService.saldoTotalPorUsuario(idUsuarioInformado);

        // Verificando
        assertThat(valorTotal).isEqualTo(BigDecimal.valueOf(1300.00));

    }

    private List<Saldo> saldosCriados(){
        List<Saldo> saldos = new ArrayList<>();
        saldos.add(Saldo.builder().id(1L).deposito(BigDecimal.valueOf(200.00))
                .dataDeposito(LocalDateTime.parse("30/06/2022 13:00:00",DTFHORA)).build());
        saldos.add(Saldo.builder().id(2L).deposito(BigDecimal.valueOf(500.00))
                .dataDeposito(LocalDateTime.parse("30/06/2022 13:00:00",DTFHORA)).build());
        saldos.add(Saldo.builder().id(3L).deposito(BigDecimal.valueOf(600.00))
                .dataDeposito(LocalDateTime.parse("30/06/2022 13:00:00",DTFHORA)).build());
        saldos.add(Saldo.builder().id(4L).deposito(BigDecimal.valueOf(800.00))
                .dataDeposito(LocalDateTime.parse("30/06/2022 13:00:00",DTFHORA)).build());
        return saldos;
    }

    private List<Saque> saquesCriados(){
        List<Saque> saques = new ArrayList<>();
        saques.add(Saque.builder().id(1L).valor(BigDecimal.valueOf(250.00)).build());
        saques.add(Saque.builder().id(2L).valor(BigDecimal.valueOf(550.00)).build());
        return saques;
    }

    private Page<Saldo> listSaldoParaPage(List<Saldo> saldos, PageRequest pageRequest){
        return new PageImpl<>(saldos,pageRequest,saldos.size());
    }
}
