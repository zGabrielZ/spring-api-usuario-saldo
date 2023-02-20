package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeriadoNacionalDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -6871728252703607321L;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String name;
}
