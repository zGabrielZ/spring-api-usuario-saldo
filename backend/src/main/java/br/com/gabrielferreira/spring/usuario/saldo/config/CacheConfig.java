package br.com.gabrielferreira.spring.usuario.saldo.config;
import br.com.gabrielferreira.spring.usuario.saldo.cache.KeyCustomizadaGenerator;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig extends CachingConfigurerSupport {

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new KeyCustomizadaGenerator();
    }

}
