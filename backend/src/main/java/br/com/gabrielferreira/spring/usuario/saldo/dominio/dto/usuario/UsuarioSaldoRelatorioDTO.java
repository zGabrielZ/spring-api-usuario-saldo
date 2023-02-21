package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoViewDTO;

public record UsuarioSaldoRelatorioDTO(

        UsuarioDepositoViewDTO usuario,

        SaldoViewDTO saldo,

        UsuarioDepositoViewDTO usuarioDepositante

) {
}
