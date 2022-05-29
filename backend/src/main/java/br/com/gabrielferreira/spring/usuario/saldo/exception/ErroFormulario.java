package br.com.gabrielferreira.spring.usuario.saldo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErroFormulario implements Serializable {

    private static final long serialVersionUID = -2689179756669507572L;

    private String campo;
    private String mensagem;
}
