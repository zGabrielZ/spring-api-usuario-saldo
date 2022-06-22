package br.com.gabrielferreira.spring.usuario.saldo.exception.modelo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ErroPadrao implements Serializable {
    private static final long serialVersionUID = -2107726998936765804L;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataErro;
    private Integer status;
    private String erro;
    private String mensagem;
    private List<ErroFormulario> erroFormularios;
}
