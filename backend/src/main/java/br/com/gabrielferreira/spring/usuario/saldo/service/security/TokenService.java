package br.com.gabrielferreira.spring.usuario.saldo.service.security;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class TokenService {

    private final String expiracao;
    private final String senhaSecreta;

    public TokenService(@Value("${forum.jwt.expiration}") String expiracao, @Value("${forum.jwt.secret}") String senhaSecreta) {
        this.expiracao = expiracao;
        this.senhaSecreta = senhaSecreta;
    }

    public String gerarToken(Authentication authentication){

        Usuario usuario = (Usuario) authentication.getPrincipal();
        Date dataAtual = new Date();
        Date dataExpiracao = new Date(dataAtual.getTime() + Long.parseLong(expiracao));

        return Jwts.builder()
                .setIssuer("API Usuário Saldos") // Quem foi que fez esse token
                .setSubject(usuario.getId().toString()) // Usuário autenticado
                .setIssuedAt(dataAtual) // Data que foi gerado
                .setExpiration(dataExpiracao) // Expiração do token
                .signWith(SignatureAlgorithm.HS256 , senhaSecreta) // Token criptografado
                .compact();
    }

    public boolean isTokenValido(String token){
        try {
            Jwts.parser().setSigningKey(senhaSecreta).parseClaimsJws(token); // Verificar se está ok o token
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public Long recuperarIdUsuarioLogado(String token){
        Claims claims = Jwts.parser().setSigningKey(senhaSecreta).parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }

}
