package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosDTO(
        String nome,
        String cpf,
        LocalDate dataNascimento,
        BigDecimal totalSaques,
        BigDecimal totalDeposito,
        BigDecimal totalSaldo
) {

}
