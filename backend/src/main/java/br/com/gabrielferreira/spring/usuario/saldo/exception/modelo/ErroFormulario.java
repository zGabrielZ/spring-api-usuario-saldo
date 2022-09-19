package br.com.gabrielferreira.spring.usuario.saldo.exception.modelo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@AllArgsConstructor
public class ErroFormulario implements Serializable {

    @Serial
    private static final long serialVersionUID = -2689179756669507572L;

    private String campo;
    private String mensagem;
}
