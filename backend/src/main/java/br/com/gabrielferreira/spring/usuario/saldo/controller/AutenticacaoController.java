package br.com.gabrielferreira.spring.usuario.saldo.controller;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.autenticacao.AutenticacaoFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.autenticacao.TokenDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory.AutenticacaoDTOFactory;
import br.com.gabrielferreira.spring.usuario.saldo.service.security.TokenService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/autenticacao")
public class AutenticacaoController {

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    @ApiOperation("Autenticou um usuário")
    @ResponseStatus(code = HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201,message = "Autenticou um usuário"),
            @ApiResponse(code = 400,message = "Ocorreu um erro personalizado"),
    })
    @PostMapping
    public ResponseEntity<TokenDTO> autenticar(@Valid @RequestBody AutenticacaoFormDTO autenticacaoFormDTO){
        UsernamePasswordAuthenticationToken dadosLogin = AutenticacaoDTOFactory.toUsernamePassword(autenticacaoFormDTO);
        Authentication authentication =  authenticationManager.authenticate(dadosLogin);
        String token = tokenService.gerarToken(authentication);
        return ResponseEntity.ok(AutenticacaoDTOFactory.toTokenDTO(token, "Bearer"));
    }

}
