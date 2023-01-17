package br.com.gabrielferreira.spring.usuario.saldo.config.security;
import br.com.gabrielferreira.spring.usuario.saldo.exception.handler.ServiceHandlerAutenticacao;
import br.com.gabrielferreira.spring.usuario.saldo.exception.handler.ServiceHandlerPermissao;
import br.com.gabrielferreira.spring.usuario.saldo.service.UsuarioService;
import br.com.gabrielferreira.spring.usuario.saldo.service.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.enums.RoleEnum.*;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;

    private final UsuarioService usuarioService;

    private static final String[] PUBLICO_ENDPOINT_POST = {
            "/autenticacao",
            "/usuarios"
    };

    private static final String PUBLICO_ENDPOINT_H2 = "/h2-console/**";

    // Config a partir da autenticação
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Config de Autorização
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeRequests()
                .antMatchers(HttpMethod.POST,PUBLICO_ENDPOINT_POST).permitAll()
                .antMatchers(HttpMethod.GET,PUBLICO_ENDPOINT_H2).permitAll()
                .antMatchers(HttpMethod.GET, "/usuarios").hasAnyRole(ROLE_ADMIN.getRoleResumida(), ROLE_FUNCIONARIO.getRoleResumida())
                .antMatchers(HttpMethod.POST, "/saldos/depositar").hasAnyRole(ROLE_ADMIN.getRoleResumida())
                .antMatchers(HttpMethod.DELETE, "/usuarios/*").hasAnyRole(ROLE_ADMIN.getRoleResumida())
                .anyRequest().authenticated()
                .and().csrf().disable() // Disable csrf, via token fica livre disso
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Não é pra criar sessão
                .and().addFilterBefore(new AutenticacaoTokenFilter(tokenService, usuarioService), UsernamePasswordAuthenticationFilter.class) // Adicionando o filtro antes de qualquer coisa
                .exceptionHandling().authenticationEntryPoint(new ServiceHandlerAutenticacao()) // Mensagem personalizada quando não for autenticado
                .and().exceptionHandling().accessDeniedHandler(new ServiceHandlerPermissao()) // Mensagem personalizada quando não tiver permissão
                .and().build();
    }

    // Config de recurso estaticos
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().antMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**", "/swagger-resources/**", PUBLICO_ENDPOINT_H2));
    }

}
