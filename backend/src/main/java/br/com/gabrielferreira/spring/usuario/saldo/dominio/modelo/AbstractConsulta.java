package br.com.gabrielferreira.spring.usuario.saldo.dominio.modelo;

import com.querydsl.core.types.Path;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AbstractConsulta implements Serializable {

    @Serial
    private static final long serialVersionUID = -7869797377313657930L;

    private String atributo;

    private String alias;

    private Path<?> path;
}
