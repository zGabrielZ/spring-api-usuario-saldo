package br.com.gabrielferreira.spring.usuario.saldo.repositorio;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saldo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SaldoRepositorio extends JpaRepository<Saldo,Long> {

    @Query("SELECT s.deposito as deposito FROM Saldo s " +
            "WHERE s.usuario.id = :idUsuario")
    List<BigDecimal> findByValorByUsuario(@Param("idUsuario") Long idUsuario);
}
