package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.pefil.PerfilDetailsDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDetailsDTO implements Serializable, UserDetails {

    @Serial
    private static final long serialVersionUID = -7061711027863603940L;

    private Long id;

    private String email;

    private String senha;

    private List<PerfilDetailsDTO> perfis = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return perfis;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
