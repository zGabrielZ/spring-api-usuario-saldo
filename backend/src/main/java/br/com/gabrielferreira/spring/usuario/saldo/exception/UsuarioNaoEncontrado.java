package br.com.gabrielferreira.spring.usuario.saldo.exception;

public class UsuarioNaoEncontrado extends RuntimeException{
    private static final long serialVersionUID = -1166830111291032049L;

    public UsuarioNaoEncontrado(String mensagem){
        super(mensagem);
    }
}
