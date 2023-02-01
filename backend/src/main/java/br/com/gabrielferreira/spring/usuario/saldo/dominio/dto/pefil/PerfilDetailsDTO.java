package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.pefil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerfilDetailsDTO implements Serializable, GrantedAuthority {

    @Serial
    private static final long serialVersionUID = -7061711027863603940L;

    private Long id;

    private String descricao;

    private String nome;

    @Override
    public String getAuthority() {
        return nome;
    }
}
