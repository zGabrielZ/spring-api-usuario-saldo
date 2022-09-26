package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.autenticacao.AutenticacaoFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.autenticacao.TokenDTO;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class AutenticacaoDTOFactory {

    private AutenticacaoDTOFactory(){}

    public static UsernamePasswordAuthenticationToken toUsernamePassword(AutenticacaoFormDTO autenticacaoFormDTO){
        return new UsernamePasswordAuthenticationToken(autenticacaoFormDTO.getEmail(), autenticacaoFormDTO.getSenha());
    }

    public static TokenDTO toTokenDTO(String token, String tipoToken){
        return TokenDTO.builder()
                .tokenGerado(token)
                .tipoToken(tipoToken)
                .build();
    }

}
