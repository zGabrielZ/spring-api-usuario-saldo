package br.com.gabrielferreira.spring.usuario.saldo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class ClockConfig {

    private static final String AMERICA_SAO_PAULO = "America/Sao_Paulo";

    @Bean
    public Clock getClock(){
        return Clock.system(ZoneId.of(AMERICA_SAO_PAULO));
    }
}
