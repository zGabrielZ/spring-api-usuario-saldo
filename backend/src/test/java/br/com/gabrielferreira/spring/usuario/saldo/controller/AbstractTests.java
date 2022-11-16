package br.com.gabrielferreira.spring.usuario.saldo.controller;
import java.time.format.DateTimeFormatter;

public abstract class AbstractTests {

    protected static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    protected static final String PORTA = "http://localhost:8080";
}
