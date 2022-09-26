package br.com.gabrielferreira.spring.usuario.saldo.controller;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.autenticacao.AutenticacaoFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.autenticacao.TokenDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory.AutenticacaoDTOFactory;
import br.com.gabrielferreira.spring.usuario.saldo.service.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/autenticacao")
public class AutenticacaoController {

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    @PostMapping
    public ResponseEntity<TokenDTO> autenticar(@Valid @RequestBody AutenticacaoFormDTO autenticacaoFormDTO){
        UsernamePasswordAuthenticationToken dadosLogin = AutenticacaoDTOFactory.toUsernamePassword(autenticacaoFormDTO);
        Authentication authentication =  authenticationManager.authenticate(dadosLogin);
        String token = tokenService.gerarToken(authentication);
        return ResponseEntity.ok(AutenticacaoDTOFactory.toTokenDTO(token, "Bearer"));
    }

}
