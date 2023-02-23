package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.relatorio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDepositoRelatorioDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 5649141200664469763L;

    private String nomeUsuario;

    private String emailUsuario;

    private String cpfUsuario;

    private String nomeUsuarioDepositante;

    private String emailUsuarioDepositante;

    private String cpfUsuarioDepositante;
}
