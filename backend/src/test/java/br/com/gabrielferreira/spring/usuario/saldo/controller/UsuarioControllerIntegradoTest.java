package br.com.gabrielferreira.spring.usuario.saldo.controller;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SaqueViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Perfil;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saldo;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saque;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.exception.modelo.ErroPadrao;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.PerfilRepositorio;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.SaldoRepositorio;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.SaqueRepositorio;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.UsuarioRepositorio;
import br.com.gabrielferreira.spring.usuario.saldo.utils.RestResponsePage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static br.com.gabrielferreira.spring.usuario.saldo.utils.MascarasUtils.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UsuarioControllerIntegradoTest extends AbstractController {

    private static String TOKEN_USUARIO_LOGADO = null;
    private static Long ID_USUARIO_LOGADO = null;
    private static final String API = "/usuarios";

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Autowired
    PerfilRepositorio perfilRepositorio;

    @Autowired
    SaldoRepositorio saldoRepositorio;

    @Autowired
    SaqueRepositorio saqueRepositorio;

    @Test
    @DisplayName("Deve realizar a consulta de saldos por usuário")
    void deveRealizarConsultaDeSaldosPorUsuario(){
        // Executando
        String caminho = PORTA.concat(API).concat("/").concat(ID_USUARIO_LOGADO.toString()).concat("/saldos")
                .concat("?pagina=0").concat("&quantidadeRegistro=5")
                .concat("&sort=id,desc").concat("&sort=deposito,desc")
                .concat("&sort=data,desc");
        ParameterizedTypeReference<RestResponsePage<SaldoViewDTO>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<RestResponsePage<SaldoViewDTO>> request = genericRestTemplate.genericRequest(null, responseType, JSON_MEDIATYPE, HttpMethod.GET, caminho, TOKEN_USUARIO_LOGADO, testRestTemplate);

        // Verificando
        assertThat(request.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Deve realizar a consulta de saques por usuário")
    void deveRealizarConsultaDeSaquesPorUsuario(){
        // Executando
        String caminho = PORTA.concat(API).concat("/").concat(ID_USUARIO_LOGADO.toString()).concat("/saques")
                .concat("?pagina=0").concat("&quantidadeRegistro=5")
                .concat("&sort=id,desc");
        ParameterizedTypeReference<RestResponsePage<SaqueViewDTO>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<RestResponsePage<SaqueViewDTO>> request = genericRestTemplate.genericRequest(null, responseType, JSON_MEDIATYPE, HttpMethod.GET, caminho, TOKEN_USUARIO_LOGADO, testRestTemplate);

        // Verificando
        assertThat(request.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Deve realizar a consulta de usuários")
    void deveRealizarConsultaDeUsuarios(){
        // Executando
        String caminho = PORTA.concat(API)
                .concat("?pagina=0").concat("&quantidadeRegistro=5")
                .concat("&sort=nome,desc").concat("&sort=dataNascimento,desc");
        ParameterizedTypeReference<RestResponsePage<UsuarioViewDTO>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<RestResponsePage<UsuarioViewDTO>> request = genericRestTemplate.genericRequest(null, responseType, JSON_MEDIATYPE, HttpMethod.GET, caminho, TOKEN_USUARIO_LOGADO, testRestTemplate);

        // Verificando
        assertThat(request.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Não deve realizar a consulta de saldos por usuário quando não informar a direção")
    void naoDeveRealizarConsultaDeSaldosPorUsuarioQuandoNaoInformarDirecao(){
        // Executando
        String caminho = PORTA.concat(API).concat("/").concat(ID_USUARIO_LOGADO.toString()).concat("/saldos")
                .concat("?pagina=0").concat("&quantidadeRegistro=5")
                .concat("&sort=id");
        ParameterizedTypeReference<ErroPadrao> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<ErroPadrao> request = genericRestTemplate.genericRequest(null, responseType, JSON_MEDIATYPE, HttpMethod.GET, caminho, TOKEN_USUARIO_LOGADO, testRestTemplate);

        // Verificando
        assertThat(request.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Não deve realizar a consulta de saldos por usuário quando informar a direção incorreta")
    void naoDeveRealizarConsultaDeSaldosPorUsuarioQuandoInformarDirecaoIncorreta(){
        // Executando
        String caminho = PORTA.concat(API).concat("/").concat(ID_USUARIO_LOGADO.toString()).concat("/saldos")
                .concat("?pagina=0").concat("&quantidadeRegistro=5")
                .concat("&sort=id,test");
        ParameterizedTypeReference<ErroPadrao> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<ErroPadrao> request = genericRestTemplate.genericRequest(null, responseType, JSON_MEDIATYPE, HttpMethod.GET, caminho, TOKEN_USUARIO_LOGADO, testRestTemplate);

        // Verificando
        assertThat(request.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @BeforeAll
    void deveLogarUsuario(){
        Usuario usuario = gerarUsuario();
        ID_USUARIO_LOGADO = usuario.getId();
        TOKEN_USUARIO_LOGADO = "Bearer ".concat(getTokenUsuarioLogado(usuario.getEmail(), "123"));
    }

    private Usuario gerarUsuario(){
        Perfil perfilAdmin = perfilRepositorio.findById(1L).orElseThrow();
        Perfil perfilFuncionario = perfilRepositorio.findById(2L).orElseThrow();

        Usuario usuario = gerarUsuario(Arrays.asList(perfilAdmin, perfilFuncionario), "Marcio Ferreira", "marcio@gmail.com", "$2a$10$aXn3.Hn6MbnsdEbvBh/OhehvXBo3HMyPIqGoBVRuU2yxxNxqY0tWu", "33288179059", LocalDate.parse("10/12/1995",DATA_FORMATTER)
                , BigDecimal.valueOf(10000.00), false);

        usuarioRepositorio.save(usuario);

        Saldo saldo1 = gerarSaldo(BigDecimal.valueOf(500.00), LocalDateTime.now(), usuario, usuario);
        Saldo saldo2 = gerarSaldo(BigDecimal.valueOf(800.00), LocalDateTime.now(), usuario, usuario);
        Saldo saldo3 = gerarSaldo(BigDecimal.valueOf(400.00), LocalDateTime.now(), usuario, usuario);

        List<Saldo> saldos = Arrays.asList(saldo1, saldo2, saldo3);
        saldos.forEach(s -> saldoRepositorio.save(s));

        Saque saque = gerarSaque(BigDecimal.valueOf(150.00), LocalDateTime.now(), usuario);
        saqueRepositorio.save(saque);

        return usuario;
    }

}
