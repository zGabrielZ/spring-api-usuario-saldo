package br.com.gabrielferreira.spring.usuario.saldo.controller;
import br.com.gabrielferreira.spring.usuario.saldo.config.GenericRestTemplate;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.autenticacao.AutenticacaoFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.autenticacao.TokenDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Perfil;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.PerfilRepositorio;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.UsuarioRepositorio;
import br.com.gabrielferreira.spring.usuario.saldo.utils.AbstractTests;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class AutenticacaoControllerTest extends AbstractTests {

    private static final String API = "/autenticacao";

    private static final MediaType JSON_MEDIATYPE = MediaType.APPLICATION_JSON;

    @Autowired
    private GenericRestTemplate genericRestTemplate;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private PerfilRepositorio perfilRepositorio;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @DisplayName("Não deve fazer o login na aplicação quando informar a senha incorreta")
    @Order(1)
    void naoDeveLogar(){
        // Cenário
        AutenticacaoFormDTO autenticacaoFormDTO = AutenticacaoFormDTO.builder()
                .email("ferreiragabriel261211222@gmail.com")
                .senha("1233333")
                .build();

        // Executando
        String caminho = PORTA.concat(API);
        ParameterizedTypeReference<TokenDTO> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<TokenDTO> request = genericRestTemplate.genericRequest(autenticacaoFormDTO, responseType, JSON_MEDIATYPE, HttpMethod.POST, caminho, "", testRestTemplate);

        // Verificando
        assertThat(request.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Deve fazer o login na aplicação")
    @Order(2)
    void deveLogar(){
        // Cenário
        AutenticacaoFormDTO autenticacaoFormDTO = AutenticacaoFormDTO.builder()
                .email("ferreiragabriel2612@gmail.com")
                .senha("123")
                .build();

        gerarUsuario();

        // Executando
        String caminho = PORTA.concat(API);
        ParameterizedTypeReference<TokenDTO> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<TokenDTO> request = genericRestTemplate.genericRequest(autenticacaoFormDTO, responseType, JSON_MEDIATYPE, HttpMethod.POST, caminho, "", testRestTemplate);

        // Verificando
        assertThat(request.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Não deve realizar a consulta de usuário pelo id quando não informar o usuário logado")
    @Order(3)
    void naoDeveRealizarConsulta(){
        // Executando
        String caminho = PORTA.concat("/usuarios/1");
        ParameterizedTypeReference<UsuarioViewDTO> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<UsuarioViewDTO> request = genericRestTemplate.genericRequest(null, responseType, JSON_MEDIATYPE, HttpMethod.GET, caminho, "", testRestTemplate);

        // Verificando
        assertThat(request.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    private void gerarUsuario(){

        Perfil perfil = Perfil.builder().nome("ROLE_FUNCIONARIO").build();
        perfilRepositorio.save(perfil);

        Usuario usuario = Usuario.builder().nome("Gabriel Ferreira").email("ferreiragabriel2612@gmail.com").senha("$2a$10$g2AT4HFF..7JcSaxF4WhUO0RZjw5kAGy3RvBNkD/NrZ4Q2FBPHWfm")
                .cpf("73977674005").dataNascimento(LocalDate.parse("10/12/1995",DTF))
                .saldoTotal(BigDecimal.ZERO)
                .perfis(List.of(Perfil.builder().id(1L).build()))
                .build();

        usuarioRepositorio.save(usuario);
    }

}
