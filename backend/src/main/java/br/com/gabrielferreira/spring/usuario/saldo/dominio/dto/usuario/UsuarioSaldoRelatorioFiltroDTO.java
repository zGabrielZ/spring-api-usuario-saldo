package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioSaldoRelatorioFiltroDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -7136821550686871414L;

    private String nome;

    private String email;

    private String cpf;

    private BigDecimal saldoDeposito;

    private LocalDate dataInicioDeposito;

    private LocalDate dataFinalDeposito;

    private String nomeUsuarioDepositante;

    private String emailUsuarioDepositante;

    private String cpfUsuarioDepositante;

    @JsonIgnore
    public boolean isContemNome(){
        return nome != null && !nome.isBlank();
    }

    @JsonIgnore
    public boolean isContemEmail(){
        return email != null && !email.isBlank();
    }

    @JsonIgnore
    public boolean isContemCpf(){
        return cpf != null && !cpf.isBlank();
    }

    @JsonIgnore
    public boolean isContemSaldoDeposito(){
        return saldoDeposito != null;
    }

    @JsonIgnore
    public boolean isContemDataInicioDeposito(){
        return dataInicioDeposito != null;
    }

    @JsonIgnore
    public boolean isContemDataFinalDeposito(){
        return dataFinalDeposito != null;
    }

    @JsonIgnore
    public boolean isContemNomeDepositante(){
        return nomeUsuarioDepositante != null && !nomeUsuarioDepositante.isBlank();
    }

    @JsonIgnore
    public boolean isContemEmailDepositante(){
        return emailUsuarioDepositante != null && !emailUsuarioDepositante.isBlank();
    }

    @JsonIgnore
    public boolean isContemCpfDepositante(){
        return cpfUsuarioDepositante != null && !cpfUsuarioDepositante.isBlank();
    }


}
