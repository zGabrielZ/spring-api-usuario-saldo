package br.com.gabrielferreira.spring.usuario.saldo.service;

import br.com.gabrielferreira.spring.usuario.saldo.entidade.Saldo;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.Saque;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.dto.SaldoFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.SaldoRepositorio;
import static org.assertj.core.api.Assertions.*;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class SaldoServiceTest {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private SaldoRepositorio saldoRepositorio;
    private UsuarioService usuarioService;
    private SaldoService saldoService;

    @BeforeEach
    void criarInstancias(){
        saldoRepositorio = mock(SaldoRepositorio.class);
        usuarioService = mock(UsuarioService.class);
        saldoService = new SaldoService(saldoRepositorio,usuarioService);
    }

    @Test
    @DisplayName("Depositar um saldo por usuário deveria salvar no banco de dados quando informar informações corretas.")
    void deveDepositar(){
        // Cenário

        Long idUsuarioInformado = 1L;
        // Mock para retornar um usuário
        Usuario usuario = usuarioCriado();
        when(usuarioService.buscarPorId(idUsuarioInformado)).thenReturn(usuario);

        // Mock para retornar um saldo quando tiver salvo
        SaldoFormDTO saldoFormDTO = criarSaldoFormDto(BigDecimal.valueOf(500.00),
                LocalDateTime.parse("30/06/2022 13:00:00",dateTimeFormatter),idUsuarioInformado);
        Saldo saldoJaSalvo = Saldo.builder().id(1L).deposito(saldoFormDTO.getDeposito()).dataDeposito(saldoFormDTO.getDataDeposito())
                .usuario(usuario).build();
        when(saldoRepositorio.save(any())).thenReturn(saldoJaSalvo);

        // Executando
        Saldo saldoResultado = saldoService.depositar(saldoFormDTO);

        // Verificando
        assertThat(saldoResultado.getId()).isNotNull();
        assertThat(saldoResultado.getDeposito()).isEqualTo(BigDecimal.valueOf(500.00));
        assertThat(saldoResultado.getDataDeposito()).isEqualTo(LocalDateTime.parse("30/06/2022 13:00:00",dateTimeFormatter));
        assertThat(saldoResultado.getUsuario().getNome()).isEqualTo("José Ferreira");
    }

    @Test
    @DisplayName("Depositar saldo não deveria salvar quando a quantidade for menor do que 0.")
    void naoDeveDepositarSaldoMenorQueZero(){
        // Cenário
        SaldoFormDTO saldoFormDTO = criarSaldoFormDto(BigDecimal.valueOf(-10.0),
                LocalDateTime.parse("30/06/2022 13:00:00",dateTimeFormatter),any());

        // Executando e verificando
        assertThrows(ExcecaoPersonalizada.class, () -> saldoService.depositar(saldoFormDTO));
        verify(saldoRepositorio,never()).save(any());
    }

    @Test
    @DisplayName("Depositar saldo não deveria salvar quando a quantidade for igual a 0.")
    void naoDeveDepositarSaldoIgualZero(){
        // Cenário
        SaldoFormDTO saldoFormDTO = criarSaldoFormDto(BigDecimal.valueOf(0.0),
                LocalDateTime.parse("30/06/2022 13:00:00",dateTimeFormatter),any());

        // Executando e verificando
        assertThrows(ExcecaoPersonalizada.class, () -> saldoService.depositar(saldoFormDTO));
        verify(saldoRepositorio,never()).save(any());
    }

    @Test
    @DisplayName("Saldos por usuário deveria retornar uma lista quando informar o usuário já cadastrado.")
    void deveMostrarSaldosPorUsuario(){
        // Cenário
        Long idUsuarioPesquisar = 1L;
        // Mock para retornar um usuário com saldos
        Usuario usuario = usuarioCriado();
        List<Saldo> saldos = saldosCriados(usuario);
        saldos.forEach(usuario::adicionarSaldo);
        when(usuarioService.buscarPorId(idUsuarioPesquisar)).thenReturn(usuario);

        // Executando
        PageRequest pageRequest = PageRequest.of(0,2, Sort.Direction.DESC,"dataDeposito");
        Page<Saldo> saldosResultados = saldoService.saldosPorUsuario(idUsuarioPesquisar,pageRequest);

        // Verificando
        assertThat(saldosResultados).isNotEmpty();
        assertThat(saldosResultados.getTotalElements()).isEqualTo(4);
        assertThat(saldosResultados.getTotalPages()).isEqualTo(2);
        assertThat(saldosResultados.getSize()).isEqualTo(2);
    }

    @Test
    @DisplayName("Saldos por usuário deveria retornar uma lista vazia quando não tiver saldos associado ao usuário pesquisado.")
    void deveMostrarSaldosVazioPorUsuarioNaoEncontrado(){
        // Cenário
        Long idUsuarioPesquisar = 1L;

        // Mock para retornar usuario quando informar o id
        Usuario usuario = usuarioCriado();
        when(usuarioService.buscarPorId(idUsuarioPesquisar)).thenReturn(usuario);

        // Executando e verificando
        PageRequest pageRequest = PageRequest.of(0,2, Sort.Direction.DESC,"dataDeposito");
        Page<Saldo> saldosResultados = saldoService.saldosPorUsuario(idUsuarioPesquisar,pageRequest);

        // Verificando
        assertThat(saldosResultados).isEmpty();
    }

    @Test
    @DisplayName("Saldo total por usuário deveria retornar um valor quando informar um usuário.")
    void deveMostrarSaldoTotalPorUsuario(){
        // Cenário
        Usuario usuario = usuarioCriado();
        List<Saldo> saldos = saldosCriados(usuario);
        saldos.forEach(usuario::adicionarSaldo);

        // Executando
        BigDecimal valorTotal = saldoService.saldoTotalPorUsuario(usuario);

        // Verificando
        assertThat(valorTotal).isEqualTo(BigDecimal.valueOf(2100.00));

    }

    @Test
    @DisplayName("Saldo total por usuário deveria retornar um valor quando informar um usuário que já tem seus saques registrados.")
    void deveMostrarSaldoTotalPorUsuarioQuandoTiverSaque(){
        // Cenário
        Usuario usuario = usuarioCriado();

        List<Saldo> saldos = saldosCriados(usuario);
        saldos.forEach(usuario::adicionarSaldo);

        List<Saque> saques = saqueCriados(usuario);
        saques.forEach(usuario::adicionarSaque);

        // Executando
        BigDecimal valorTotal = saldoService.saldoTotalPorUsuario(usuario);

        // Verificando
        assertThat(valorTotal).isEqualTo(BigDecimal.valueOf(1300.00));

    }

    private SaldoFormDTO criarSaldoFormDto(BigDecimal deposito, LocalDateTime dataDeposito, Long idUsuario){
        return SaldoFormDTO.builder().deposito(deposito).dataDeposito(dataDeposito).idUsuario(idUsuario).build();
    }

    private Usuario usuarioCriado(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return Usuario.builder().id(1L).nome("José Ferreira").email("jose@gmail.com")
                .senha("$2a$10$rkFB6IzKB9M/T8UBxe11eOS0dsUJxxe0.R2OLhkMqFtfHdOqypwZS").cpf("73977674005")
                .dataNascimento(LocalDate.parse("10/02/1990",dateTimeFormatter))
                .saldos(new ArrayList<>()).saques(new ArrayList<>())
                .build();
    }

    private List<Saldo> saldosCriados(Usuario usuario){
        List<Saldo> saldos = new ArrayList<>();
        saldos.add(Saldo.builder().id(1L).deposito(BigDecimal.valueOf(200.00))
                .dataDeposito(LocalDateTime.parse("30/06/2022 13:00:00",dateTimeFormatter)).usuario(usuario).build());
        saldos.add(Saldo.builder().id(2L).deposito(BigDecimal.valueOf(500.00))
                .dataDeposito(LocalDateTime.parse("30/06/2022 13:00:00",dateTimeFormatter)).usuario(usuario).build());
        saldos.add(Saldo.builder().id(3L).deposito(BigDecimal.valueOf(600.00))
                .dataDeposito(LocalDateTime.parse("30/06/2022 13:00:00",dateTimeFormatter)).usuario(usuario).build());
        saldos.add(Saldo.builder().id(4L).deposito(BigDecimal.valueOf(800.00))
                .dataDeposito(LocalDateTime.parse("30/06/2022 13:00:00",dateTimeFormatter)).usuario(usuario).build());
        return saldos;
    }

    private List<Saque> saqueCriados(Usuario usuario){
        List<Saque> saques = new ArrayList<>();
        saques.add(Saque.builder().id(1L).valor(BigDecimal.valueOf(250.00)).usuario(usuario).build());
        saques.add(Saque.builder().id(2L).valor(BigDecimal.valueOf(550.00)).usuario(usuario).build());
        return saques;
    }

    private Page<Saldo> listParaPage(List<Saldo> saldos, PageRequest pageRequest){
        return new PageImpl<>(saldos,pageRequest,saldos.size());
    }
}
