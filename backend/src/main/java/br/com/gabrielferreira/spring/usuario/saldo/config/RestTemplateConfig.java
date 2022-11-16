package br.com.gabrielferreira.spring.usuario.saldo.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.Serial;
import java.io.Serializable;

@Configuration
public class RestTemplateConfig implements Serializable {

    @Serial
    private static final long serialVersionUID = -5072026192195059466L;

    @Bean
    public RestTemplate getRestTemplate(RestTemplateBuilder builder){
        return builder.build();
    }

}
