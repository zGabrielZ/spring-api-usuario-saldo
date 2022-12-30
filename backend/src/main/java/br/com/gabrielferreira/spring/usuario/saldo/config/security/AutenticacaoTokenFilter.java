package br.com.gabrielferreira.spring.usuario.saldo.config.security;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.service.UsuarioService;
import br.com.gabrielferreira.spring.usuario.saldo.service.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class AutenticacaoTokenFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    private final UsuarioService usuarioService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain) throws ServletException, IOException {

        // Recuperar Token

        String token = recuperarToken(request);

        // Verificar esse token se está valido

        boolean isValido = tokenService.isTokenValido(token);

        // Autenticar o usuário caso for valido

        if(isValido){
            autenticarUsuario(token);
        }


        filterChain.doFilter(request,response);
    }

    private String recuperarToken(HttpServletRequest request){
        String headerToken = request.getHeader("Authorization");
        if(StringUtils.isBlank(headerToken) || !headerToken.startsWith("Bearer ")){
            return null;
        }

        return headerToken.substring(7);
    }

    private void autenticarUsuario(String token){
        Long idUsuario = recuperarUsuario(token);
        Usuario usuario = usuarioService.buscarUsuarioAutenticado(idUsuario);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getPerfis());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private Long recuperarUsuario(String token){
        return tokenService.recuperarIdUsuarioLogado(token);
    }

}
