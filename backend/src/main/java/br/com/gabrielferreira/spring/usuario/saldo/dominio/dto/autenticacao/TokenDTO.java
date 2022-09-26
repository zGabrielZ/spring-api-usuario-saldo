package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.autenticacao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -357454598872222620L;

    private String tokenGerado;
    private String tipoToken;

}
