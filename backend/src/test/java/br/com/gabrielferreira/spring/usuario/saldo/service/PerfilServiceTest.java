package br.com.gabrielferreira.spring.usuario.saldo.service;

import br.com.gabrielferreira.spring.usuario.saldo.controller.AbstractTests;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Perfil;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class PerfilServiceTest extends AbstractTests {

    private PerfilService perfilService;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void criarInstancias() {
        perfilService = new PerfilService();
    }

    @Test
    @DisplayName("Deve recuperar o usuário quando tiver logado.")
    void deveRecuperarUsuario(){
        // Cenário
        when(authentication.getCredentials()).thenReturn("mockAutenticacao");
        when(authentication.getPrincipal()).thenReturn(gerarUsuario(2L));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Executando
        Usuario usuarioLogado = perfilService.recuperarUsuarioLogado();

        // Verificando Usuário
        assertThat(usuarioLogado).isNotNull();
    }

    @Test
    @DisplayName("Não deve recuperar o usuário quando não tiver logado.")
    void naoDeveRecuperarUsuario(){
        // Cenário
        when(authentication.getCredentials()).thenReturn("anonymousUser");
        when(authentication.getPrincipal()).thenReturn(null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Executando
        Usuario usuarioLogado = perfilService.recuperarUsuarioLogado();

        // Verificando Usuário
        assertThat(usuarioLogado).isNull();
    }

    @Test
    @DisplayName("Deve conter usuário com a conta adminstração quando tiver logado.")
    void deveConterUsuarioLogadoAdmin(){
        // Cenário
        when(authentication.getCredentials()).thenReturn("mockAutenticacao");
        when(authentication.getPrincipal()).thenReturn(gerarUsuario(1L));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Executando
        boolean isAdmin = perfilService.isContemPerfilAdminUsuarioLogado();

        // Verificando Usuário
        assertThat(isAdmin).isTrue();
    }

    @Test
    @DisplayName("Não deve conter usuário com a conta adminstração quando tiver logado.")
    void naoDeveConterUsuarioLogadoAdmin(){
        // Cenário
        when(authentication.getCredentials()).thenReturn("anonymousUser");
        when(authentication.getPrincipal()).thenReturn(null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Executando
        boolean isAdmin = perfilService.isContemPerfilAdminUsuarioLogado();

        // Verificando Usuário
        assertThat(isAdmin).isFalse();
    }

    @Test
    @DisplayName("Deve conter usuário com a conta cliente quando tiver logado.")
    void deveConterUsuarioLogadoCliente(){
        // Cenário
        when(authentication.getCredentials()).thenReturn("mockAutenticacao");
        when(authentication.getPrincipal()).thenReturn(gerarUsuario(3L));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Executando
        boolean isCliente = perfilService.isContemPerfilClienteUsuarioLogado();

        // Verificando Usuário
        assertThat(isCliente).isTrue();
    }

    @Test
    @DisplayName("Não deve conter usuário com a conta cliente quando tiver logado.")
    void naoDeveConterUsuarioLogadoCliente(){
        // Cenário
        when(authentication.getCredentials()).thenReturn("anonymousUser");
        when(authentication.getPrincipal()).thenReturn(null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Executando
        boolean isCliente = perfilService.isContemPerfilClienteUsuarioLogado();

        // Verificando Usuário
        assertThat(isCliente).isFalse();
    }

    private Usuario gerarUsuario(Long idPerfil){
        Perfil perfil = Perfil.builder().id(idPerfil).nome("Teste").build();

        return Usuario.builder().id(1L).nome("Gabriel Ferreira").email("ferreiragabriel2612@gmail.com").senha("$2a$10$g2AT4HFF..7JcSaxF4WhUO0RZjw5kAGy3RvBNkD/NrZ4Q2FBPHWfm")
                .cpf("73977674005").dataNascimento(LocalDate.parse("10/12/1995",DTF))
                .saldoTotal(BigDecimal.ZERO)
                .perfis(List.of(perfil))
                .build();
    }

}
