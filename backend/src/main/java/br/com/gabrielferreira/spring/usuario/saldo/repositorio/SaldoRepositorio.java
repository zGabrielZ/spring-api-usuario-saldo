package br.com.gabrielferreira.spring.usuario.saldo.repositorio;

import br.com.gabrielferreira.spring.usuario.saldo.entidade.Saldo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaldoRepositorio extends JpaRepository<Saldo,Long> {
}
