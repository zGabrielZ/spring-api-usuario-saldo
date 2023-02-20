package br.com.gabrielferreira.spring.usuario.saldo.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static br.com.gabrielferreira.spring.usuario.saldo.utils.MascarasUtils.*;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {

        return builder -> {

            builder.serializers(new LocalDateSerializer(DATA_FORMATTER));
            builder.deserializers(new LocalDateDeserializer(DATA_FORMATTER));

            builder.deserializers(new LocalDateTimeDeserializer(DATA_HORA_FORMATTER));
            builder.serializers(new LocalDateTimeSerializer(DATA_HORA_FORMATTER));
        };
    }

}
