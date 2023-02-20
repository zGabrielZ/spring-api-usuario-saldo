package br.com.gabrielferreira.spring.usuario.saldo.controller;
import br.com.gabrielferreira.spring.usuario.saldo.config.GenericRestTemplate;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.autenticacao.AutenticacaoFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.autenticacao.TokenDTO;
import br.com.gabrielferreira.spring.usuario.saldo.utils.AbstractUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class AbstractController extends AbstractUtils {

    protected static final String PORTA = "http://localhost:8081";
    protected static final MediaType JSON_MEDIATYPE = MediaType.APPLICATION_JSON;

    @Autowired
    protected GenericRestTemplate genericRestTemplate;

    @Autowired
    protected TestRestTemplate testRestTemplate;

    protected String getTokenUsuarioLogado(String email, String senha){
        AutenticacaoFormDTO autenticacaoFormDTO = AutenticacaoFormDTO.builder()
                .email(email)
                .senha(senha)
                .build();

        String caminho = PORTA.concat("/autenticacao");
        ParameterizedTypeReference<TokenDTO> responseType = new ParameterizedTypeReference<>() {};

        ResponseEntity<TokenDTO> request = genericRestTemplate.genericRequest(autenticacaoFormDTO, responseType, JSON_MEDIATYPE, HttpMethod.POST, caminho, "", testRestTemplate);

        if(request.getBody() != null && request.getBody().getTokenGerado() != null){
            return request.getBody().getTokenGerado();
        }

        return "ERRO";
    }
}
