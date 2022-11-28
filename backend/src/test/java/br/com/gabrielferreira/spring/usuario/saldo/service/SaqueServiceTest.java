package br.com.gabrielferreira.spring.usuario.saldo.service;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoTotalViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SacarViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Perfil;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saque;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SacarFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.SaqueRepositorio;
import br.com.gabrielferreira.spring.usuario.saldo.utils.AbstractTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class SaqueServiceTest extends AbstractTests {

    private SaqueRepositorio saqueRepositorio;

    private UsuarioService usuarioService;

    private SaqueService saqueService;

    private PerfilService perfilService;

    private Clock clock;

    @BeforeEach
    void criarInstancias(){
        saqueRepositorio = mock(SaqueRepositorio.class);
        usuarioService = mock(UsuarioService.class);
        perfilService = mock(PerfilService.class);
        clock = mock(Clock.class);
        saqueService = new SaqueService(clock,saqueRepositorio,usuarioService,perfilService);
    }

    @Test
    @DisplayName("Deve sacar valor quando tiver valores na conta do usuário.")
    void deveSacarValor(){
        // Cenário

        // Criar o form saque
        Long idUsuarioInformado = 1L;
        SacarFormDTO sacarFormDTO = criarSaqueFormDto(BigDecimal.valueOf(50.00),idUsuarioInformado);

        // Recuperar Usuário logado como Admin
        when(perfilService.recuperarUsuarioLogado()).thenReturn(gerarUsuarioLogado(1L, "ROLE_ADMIN"));

        // Mock para retornar um valor do usuário informado
        SaldoTotalViewDTO saldoTotalViewDTO = SaldoTotalViewDTO.builder().saldoTotal(BigDecimal.valueOf(200.00)).build();
        when(usuarioService.buscarSaldoTotal(idUsuarioInformado)).thenReturn(saldoTotalViewDTO);

        // Mock para retornar um saque
        Saque saqueJaSalvo = Saque.builder().id(1L).dataSaque(LocalDateTime.now()).valor(sacarFormDTO.getQuantidade())
                .usuario(Usuario.builder().id(idUsuarioInformado).build()).build();
        when(saqueRepositorio.save(any())).thenReturn(saqueJaSalvo);

        // Mock para data atual fixa
        LocalDateTime dataAtualFixa = LocalDateTime.parse("06/10/2022 10:00:00", DTFHORA);
        when(clock.instant()).thenReturn(dataAtualFixa.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        saqueJaSalvo.setDataSaque(dataAtualFixa);

        // Mock para retornar o valor total do saldo
        when(usuarioService.atualizarSaldoTotal(any(),any())).thenReturn(BigDecimal.valueOf(150.00));

        // Executando
        SacarViewDTO sacarViewDTO = saqueService.sacar(sacarFormDTO);

        // Verificando
        assertThat(sacarViewDTO.getSaldoTotal()).isNotEqualTo(saqueJaSalvo.getValor());
        verify(saqueRepositorio).save(any());
    }

    @Test
    @DisplayName("Não deveria sacar valor quando tiver com o saldo 0 ou nulo.")
    void naoDeveSacarValorQuandoNaoTiverSaldo(){
        // Cenário

        // Criar o form saque
        Long idUsuarioInformado = 1L;
        SacarFormDTO sacarFormDTO = criarSaqueFormDto(BigDecimal.valueOf(50.00),idUsuarioInformado);

        // Recuperar Usuário logado como Admin
        when(perfilService.recuperarUsuarioLogado()).thenReturn(gerarUsuarioLogado(1L, "ROLE_ADMIN"));

        // Mock para retornar um valor do usuário informado
        SaldoTotalViewDTO saldoTotalViewDTO = SaldoTotalViewDTO.builder().saldoTotal(BigDecimal.valueOf(0.00)).build();
        when(usuarioService.buscarSaldoTotal(idUsuarioInformado)).thenReturn(saldoTotalViewDTO);

        // Executando e verificando
        assertThrows(ExcecaoPersonalizada.class, () -> saqueService.sacar(sacarFormDTO));
        verify(saqueRepositorio,never()).save(any());
    }

    @Test
    @DisplayName("Não deveria sacar valor quando o saldo total for menor do que o saque.")
    void naoDeveSacarValorQuandoForMaiorDoQueSaldoTotal(){
        // Cenário

        // Criar o form saque
        Long idUsuarioInformado = 1L;
        SacarFormDTO sacarFormDTO = criarSaqueFormDto(BigDecimal.valueOf(50.00),idUsuarioInformado);

        // Mock para retornar um valor do usuário informado
        SaldoTotalViewDTO saldoTotalViewDTO = SaldoTotalViewDTO.builder().saldoTotal(BigDecimal.valueOf(10.00)).build();
        when(usuarioService.buscarSaldoTotal(idUsuarioInformado)).thenReturn(saldoTotalViewDTO);

        // Recuperar Usuário logado como Admin
        when(perfilService.recuperarUsuarioLogado()).thenReturn(gerarUsuarioLogado(1L, "ROLE_ADMIN"));

        // Executando e verificando
        assertThrows(ExcecaoPersonalizada.class, () -> saqueService.sacar(sacarFormDTO));
        verify(saqueRepositorio,never()).save(any());
    }

    private SacarFormDTO criarSaqueFormDto(BigDecimal quantidade, Long idUsuario){
        return SacarFormDTO.builder().quantidade(quantidade).idUsuario(idUsuario).build();
    }

    private Usuario gerarUsuarioLogado(Long idPerfil, String nomePerfil){
        Perfil perfil = Perfil.builder().id(idPerfil).nome(nomePerfil).build();

        return Usuario.builder().id(1L).nome("Gabriel Ferreira").email("ferreiragabriel2612@gmail.com").senha("$2a$10$g2AT4HFF..7JcSaxF4WhUO0RZjw5kAGy3RvBNkD/NrZ4Q2FBPHWfm")
                .cpf("73977674005").dataNascimento(LocalDate.parse("10/12/1995",DTF))
                .saldoTotal(BigDecimal.ZERO)
                .perfis(List.of(perfil))
                .build();
    }
}
