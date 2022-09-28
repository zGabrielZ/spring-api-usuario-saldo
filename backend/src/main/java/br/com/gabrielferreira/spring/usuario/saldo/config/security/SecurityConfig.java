package br.com.gabrielferreira.spring.usuario.saldo.config.security;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.UsuarioRepositorio;
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

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;

    private final UsuarioRepositorio usuarioRepositorio;

    private static final String[] PUBLICO_ENDPOINT_POST = {
            "/autenticacao",
            "/usuarios"
    };

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
                .anyRequest().authenticated()
                .and().csrf().disable() // Disable csrf, via token fica livre disso
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Não é pra criar sessão
                .and().addFilterBefore(new AutenticacaoTokenFilter(tokenService, usuarioRepositorio), UsernamePasswordAuthenticationFilter.class) // Adicionando o filtro antes de qualquer coisa
                .build();
    }

    // Config de recurso estaticos
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().antMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**", "/swagger-resources/**"));
    }

}
