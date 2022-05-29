package br.com.gabrielferreira.spring.usuario.saldo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErroPadrao implements Serializable {
    private static final long serialVersionUID = -2107726998936765804L;

    private LocalDate dataErro;
    private Integer status;
    private String erro;
    private String mensagem;
    private List<ErroFormulario> erroFormularios;
}
