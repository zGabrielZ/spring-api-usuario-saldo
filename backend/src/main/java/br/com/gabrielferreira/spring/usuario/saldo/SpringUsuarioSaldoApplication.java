package br.com.gabrielferreira.spring.usuario.saldo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class SpringUsuarioSaldoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringUsuarioSaldoApplication.class, args);
	}

}
