package br.com.gabrielferreira.spring.usuario.saldo.controller;

import br.com.gabrielferreira.spring.usuario.saldo.entidade.Saldo;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.dto.SaldoFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.service.SaldoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class SaldoControllerTest {

    private static final String API = "/saldos";
    private static final MediaType JSON_MEDIATYPE = MediaType.APPLICATION_JSON;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SaldoService saldoService;

    @Test
    @DisplayName("Inserir um déposito para o usuário deveria retornar um status 201 quando informar os dados corretamente.")
    void depositarSaldoParaUsuario() throws Exception{
        // Cenário
        Long idUsuario = 1L;
        SaldoFormDTO saldoFormDTO = criarSaldoFormDto(BigDecimal.valueOf(500.00),
                LocalDateTime.parse("30/06/2022 13:00:00",formatter),idUsuario);

        // Mock para retornar um saldo quando tiver salvo
        Saldo saldo = criarSaldo(saldoFormDTO);
        when(saldoService.depositar(any())).thenReturn(saldo);

        // Transformar o objeto dto em json
        String json = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(saldoFormDTO);

        // Criar uma requisição via post
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API + "/depositar").accept(JSON_MEDIATYPE).contentType(JSON_MEDIATYPE).content(json);

        // Verificando
        mockMvc.perform(request)
                .andExpect(status().isCreated());
    }

    private SaldoFormDTO criarSaldoFormDto(BigDecimal deposito, LocalDateTime dataDeposito, Long idUsuario){
        return SaldoFormDTO.builder().deposito(deposito).dataDeposito(dataDeposito).idUsuario(idUsuario).build();
    }

    private Saldo criarSaldo(SaldoFormDTO saldoFormDTO){
        Usuario usuario = Usuario.builder().id(saldoFormDTO.getIdUsuario()).nome("Gabriel").email("ferreira@gmail.com").build();

        return Saldo.builder().id(1L).deposito(saldoFormDTO.getDeposito())
                .dataDeposito(saldoFormDTO.getDataDeposito()).usuario(usuario).build();
    }

}
