package br.com.gabrielferreira.spring.usuario.saldo.exception;

import java.io.Serial;

public class RecursoNaoEncontrado extends RuntimeException{

    @Serial
    private static final long serialVersionUID = -1166830111291032049L;

    public RecursoNaoEncontrado(String mensagem){
        super(mensagem);
    }
}
