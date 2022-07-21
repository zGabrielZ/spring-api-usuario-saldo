package br.com.gabrielferreira.spring.usuario.saldo.service;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoTotalViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SacarViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SaqueViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saque;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SacarFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.SaqueRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class SaqueServiceTest {

    private static final DateTimeFormatter DTFHORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private SaqueRepositorio saqueRepositorio;

    private UsuarioService usuarioService;

    private SaqueService saqueService;

    @BeforeEach
    void criarInstancias(){
        saqueRepositorio = mock(SaqueRepositorio.class);
        usuarioService = mock(UsuarioService.class);
        saqueService = new SaqueService(saqueRepositorio,usuarioService);
    }

    @Test
    @DisplayName("Deve sacar valor quando tiver valores na conta do usuário.")
    void deveSacarValor(){
        // Cenário

        // Criar o form saque
        Long idUsuarioInformado = 1L;
        SacarFormDTO sacarFormDTO = criarSaqueFormDto(BigDecimal.valueOf(50.00),idUsuarioInformado);

        // Mock para retornar um valor do usuário informado
        SaldoTotalViewDTO saldoTotalViewDTO = SaldoTotalViewDTO.builder().saldoTotal(BigDecimal.valueOf(200.00)).build();
        when(usuarioService.buscarSaldoTotal(idUsuarioInformado)).thenReturn(saldoTotalViewDTO);

        // Mock para retornar um saque
        Saque saqueJaSalvo = Saque.builder().id(1L).dataSaque(LocalDateTime.now()).valor(sacarFormDTO.getQuantidade())
                .usuario(Usuario.builder().id(idUsuarioInformado).build()).build();
        when(saqueRepositorio.save(any())).thenReturn(saqueJaSalvo);

        // Executando
        SacarViewDTO sacarViewDTO = saqueService.sacar(sacarFormDTO);

        // Verificando
        assertThat(sacarViewDTO.getSaldoTotal()).isEqualTo(saqueJaSalvo.getValor());
        verify(saqueRepositorio).save(any());
    }

    @Test
    @DisplayName("Não deveria sacar valor quando tiver com o saldo 0 ou nulo.")
    void naoDeveSacarValorQuandoNaoTiverSaldo(){
        // Cenário

        // Criar o form saque
        Long idUsuarioInformado = 1L;
        SacarFormDTO sacarFormDTO = criarSaqueFormDto(BigDecimal.valueOf(50.00),idUsuarioInformado);

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

        // Executando e verificando
        assertThrows(ExcecaoPersonalizada.class, () -> saqueService.sacar(sacarFormDTO));
        verify(saqueRepositorio,never()).save(any());
    }

    @Test
    @DisplayName("Saques por usuário deveria retornar uma lista quando informar o usuário já cadastrado.")
    void deveMostrarSaquesPorUsuario(){
        // Cenário

        Long idUsuarioInformado = 1L;
        // Mock para retornar um usuário qualquer
        UsuarioViewDTO usuarioViewDTO = UsuarioViewDTO.builder().id(idUsuarioInformado).build();
        when(usuarioService.buscarPorId(idUsuarioInformado)).thenReturn(usuarioViewDTO);

        // Mock para retornar saques do usuário
        PageRequest pageRequest = PageRequest.of(0,1, Sort.Direction.DESC,"dataSaque");
        Page<Saque> saques = listParaPage(saqueCriados(), pageRequest);
        when(saqueRepositorio.buscarPorUsuario(usuarioViewDTO.getId(),pageRequest)).thenReturn(saques);

        // Executando
        Page<SaqueViewDTO> saquesResultados = saqueService.saquesPorUsuario(idUsuarioInformado,pageRequest);

        // Verificando
        assertThat(saquesResultados).isNotEmpty();
        assertThat(saquesResultados.getTotalElements()).isEqualTo(2);
        assertThat(saquesResultados.getTotalPages()).isEqualTo(2);
        assertThat(saquesResultados.getSize()).isEqualTo(1);
    }

    @Test
    @DisplayName("Saques por usuário deveria retornar uma lista vazia quando não tiver saques associado ao usuário pesquisado.")
    void deveMostrarSaquesVazioPorUsuario(){
        // Cenário

        Long idUsuarioInformado = 1L;
        // Mock para retornar um usuário qualquer
        UsuarioViewDTO usuarioViewDTO = UsuarioViewDTO.builder().id(idUsuarioInformado).build();
        when(usuarioService.buscarPorId(idUsuarioInformado)).thenReturn(usuarioViewDTO);

        // Executando e verificando
        PageRequest pageRequest = PageRequest.of(0,1, Sort.Direction.DESC,"dataSaque");
        Page<Saque> saques = listParaPage(new ArrayList<>(), pageRequest);
        when(saqueRepositorio.buscarPorUsuario(usuarioViewDTO.getId(),pageRequest)).thenReturn(saques);

        // Executando
        Page<SaqueViewDTO> saquesResultados = saqueService.saquesPorUsuario(idUsuarioInformado,pageRequest);

        // Verificando
        assertThat(saquesResultados).isEmpty();
    }

    private SacarFormDTO criarSaqueFormDto(BigDecimal quantidade, Long idUsuario){
        return SacarFormDTO.builder().quantidade(quantidade).idUsuario(idUsuario).build();
    }

    private List<Saque> saqueCriados(){
        List<Saque> saques = new ArrayList<>();
        saques.add(Saque.builder().id(1L).valor(BigDecimal.valueOf(250.00)).build());
        saques.add(Saque.builder().id(2L).valor(BigDecimal.valueOf(550.00)).build());
        return saques;
    }

    private Page<Saque> listParaPage(List<Saque> saques, PageRequest pageRequest){
        return new PageImpl<>(saques,pageRequest,saques.size());
    }
}
