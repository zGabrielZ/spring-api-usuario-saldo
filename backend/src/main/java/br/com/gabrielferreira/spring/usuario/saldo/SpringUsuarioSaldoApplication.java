package br.com.gabrielferreira.spring.usuario.saldo;

import lombok.Generated;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableFeignClients
@Generated
@EnableCaching
public class SpringUsuarioSaldoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringUsuarioSaldoApplication.class, args);
	}

}
