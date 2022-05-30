package br.com.gabrielferreira.spring.usuario.saldo.exception;

public class RecursoNaoEncontrado extends RuntimeException{
    private static final long serialVersionUID = -1166830111291032049L;

    public RecursoNaoEncontrado(String mensagem){
        super(mensagem);
    }
}
