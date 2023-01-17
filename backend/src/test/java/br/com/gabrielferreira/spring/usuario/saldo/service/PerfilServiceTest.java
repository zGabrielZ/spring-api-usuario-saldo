//package br.com.gabrielferreira.spring.usuario.saldo.service;
//
//import br.com.gabrielferreira.spring.usuario.saldo.utils.AbstractTests;
//import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import static org.assertj.core.api.Assertions.*;
//
//import static org.mockito.Mockito.*;
//
//@ExtendWith(SpringExtension.class)
//class PerfilServiceTest extends AbstractTests {
//
//    private PerfilService perfilService;
//
//    @Mock
//    private Authentication authentication;
//
//    @BeforeEach
//    void criarInstancias() {
//        perfilService = new PerfilService();
//    }
//
//    @Test
//    @DisplayName("Deve recuperar o usuário quando tiver logado.")
//    void deveRecuperarUsuario(){
//        // Cenário
//        when(authentication.getCredentials()).thenReturn("mockAutenticacao");
//        when(authentication.getPrincipal()).thenReturn(gerarUsuarioLogado(1L, ROLE_ADMIN, 1L, "Gabriel Ferreira"));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        // Executando
//        Usuario usuarioLogado = perfilService.recuperarUsuarioLogado();
//
//        // Verificando Usuário
//        assertThat(usuarioLogado).isNotNull();
//    }
//
//    @Test
//    @DisplayName("Não deve recuperar o usuário quando não tiver logado.")
//    void naoDeveRecuperarUsuario(){
//        // Cenário
//        when(authentication.getCredentials()).thenReturn("anonymousUser");
//        when(authentication.getPrincipal()).thenReturn(null);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        // Executando
//        Usuario usuarioLogado = perfilService.recuperarUsuarioLogado();
//
//        // Verificando Usuário
//        assertThat(usuarioLogado).isNull();
//    }
//
//    @Test
//    @DisplayName("Deve conter usuário com a conta adminstração quando tiver logado.")
//    void deveConterUsuarioLogadoAdmin(){
//        // Cenário
//        when(authentication.getCredentials()).thenReturn("mockAutenticacao");
//        when(authentication.getPrincipal()).thenReturn(gerarUsuarioLogado(1L, ROLE_ADMIN, 2L, "José Marques"));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        // Executando
//        boolean isAdmin = perfilService.isContemPerfilAdminUsuarioLogado();
//
//        // Verificando Usuário
//        assertThat(isAdmin).isTrue();
//    }
//
//    @Test
//    @DisplayName("Não deve conter usuário com a conta adminstração quando tiver logado.")
//    void naoDeveConterUsuarioLogadoAdmin(){
//        // Cenário
//        when(authentication.getCredentials()).thenReturn("anonymousUser");
//        when(authentication.getPrincipal()).thenReturn(null);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        // Executando
//        boolean isAdmin = perfilService.isContemPerfilAdminUsuarioLogado();
//
//        // Verificando Usuário
//        assertThat(isAdmin).isFalse();
//    }
//
//    @Test
//    @DisplayName("Deve conter usuário com a conta cliente quando tiver logado.")
//    void deveConterUsuarioLogadoCliente(){
//        // Cenário
//        when(authentication.getCredentials()).thenReturn("mockAutenticacao");
//        when(authentication.getPrincipal()).thenReturn(gerarUsuarioLogado(3L, ROLE_CLIENTE, 5L, "Marina Marques"));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        // Executando
//        boolean isCliente = perfilService.isContemPerfilClienteUsuarioLogado();
//
//        // Verificando Usuário
//        assertThat(isCliente).isTrue();
//    }
//
//    @Test
//    @DisplayName("Não deve conter usuário com a conta cliente quando tiver logado.")
//    void naoDeveConterUsuarioLogadoCliente(){
//        // Cenário
//        when(authentication.getCredentials()).thenReturn("anonymousUser");
//        when(authentication.getPrincipal()).thenReturn(null);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        // Executando
//        boolean isCliente = perfilService.isContemPerfilClienteUsuarioLogado();
//
//        // Verificando Usuário
//        assertThat(isCliente).isFalse();
//    }
//
//}
