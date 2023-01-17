//package br.com.gabrielferreira.spring.usuario.saldo.controller;
//import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoViewDTO;
//import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoFormDTO;
//import br.com.gabrielferreira.spring.usuario.saldo.service.SaldoService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(SpringExtension.class)
//class SaldoControllerTest {
//
//    private static final String API = "/saldos";
//
//    private static final MediaType JSON_MEDIATYPE = MediaType.APPLICATION_JSON;
//
//    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
//
//    private MockMvc mockMvc;
//
//    @Mock
//    private SaldoService saldoService;
//
//    @InjectMocks
//    private SaldoController saldoController;
//
//    @BeforeEach
//    public void criarInstancias(){
//        mockMvc = MockMvcBuilders.standaloneSetup(saldoController).build();
//    }
//
//    @Test
//    @DisplayName("Inserir um déposito para o usuário deveria retornar um status 201 quando informar os dados corretamente.")
//    void depositarSaldoParaUsuario() throws Exception{
//        // Cenário
//
//        // Form do Saldo
//        SaldoFormDTO saldoFormDTO = SaldoFormDTO.builder().deposito(BigDecimal.valueOf(500.00))
//                .idUsuario(1L).build();
//
//        // Mock para retornar um saldo quando tiver salvo
//        SaldoViewDTO saldo = new SaldoViewDTO(1L,saldoFormDTO.getDeposito(),LocalDateTime.parse("27/07/2022 10:00:00", formatter));
//
//        when(saldoService.depositar(any())).thenReturn(saldo);
//
//        // Transformar o objeto dto em json
//        String json = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(saldoFormDTO);
//
//        // Criar uma requisição via post
//        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
//                .post(API + "/depositar").accept(JSON_MEDIATYPE).contentType(JSON_MEDIATYPE).content(json);
//
//        // Verificando
//        mockMvc.perform(request)
//                .andExpect(status().isCreated());
//    }
//
//}
