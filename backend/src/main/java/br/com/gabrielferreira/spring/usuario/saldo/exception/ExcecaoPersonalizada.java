package br.com.gabrielferreira.spring.usuario.saldo.exception;

public class ExcecaoPersonalizada extends RuntimeException{
    private static final long serialVersionUID = -1166830111291032049L;

    public ExcecaoPersonalizada(String mensagem){
        super(mensagem);
    }
}
