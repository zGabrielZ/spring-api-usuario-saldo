package br.com.gabrielferreira.spring.usuario.saldo.exception;

import java.io.Serial;

public class ExcecaoPersonalizada extends RuntimeException{

    @Serial
    private static final long serialVersionUID = -1166830111291032049L;

    public ExcecaoPersonalizada(String mensagem){
        super(mensagem);
    }
}
