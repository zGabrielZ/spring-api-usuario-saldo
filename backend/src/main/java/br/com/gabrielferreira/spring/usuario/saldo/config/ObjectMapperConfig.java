package br.com.gabrielferreira.spring.usuario.saldo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static br.com.gabrielferreira.spring.usuario.saldo.utils.MascarasUtils.*;

@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper getObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(new LocalDateTimeSerializer(DATA_HORA_FORMATTER));

        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }
}
